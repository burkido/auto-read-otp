package com.burkido.otp.input

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.requestFocus
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OtpInputFieldTest {

    @get:Rule
    val rule = createComposeRule()

    private val field get() = rule.onNode(isOtpField)

    private val isOtpField = SemanticsMatcher.expectValue(SemanticsProperties.ContentType, ContentType.SmsOtpCode)

    @Test
    fun `typed digits go into the state`() {
        val state = TextFieldState()
        rule.setContent { OtpInputField(state = state, otpLength = 6) }

        field.performTextInput("123")

        assertThat(state.text.toString()).isEqualTo("123")
    }

    @Test
    fun `letters are dropped while digits around them are kept`() {
        val state = TextFieldState()
        rule.setContent { OtpInputField(state = state, otpLength = 6) }

        field.performTextInput("1a2b3")

        assertThat(state.text.toString()).isEqualTo("123")
    }

    @Test
    fun `pasting a whole message keeps only the code`() {
        val state = TextFieldState()
        rule.setContent { OtpInputField(state = state, otpLength = 6) }

        field.performTextReplacement("Code: 123-456")

        assertThat(state.text.toString()).isEqualTo("123456")
    }

    @Test
    fun `input stops at otp length`() {
        val state = TextFieldState()
        rule.setContent { OtpInputField(state = state, otpLength = 4) }

        field.performTextInput("123456")

        assertThat(state.text.toString()).isEqualTo("1234")
    }

    @Test
    fun `unicode digits are rejected`() {
        val state = TextFieldState()
        rule.setContent { OtpInputField(state = state, otpLength = 6) }

        field.performTextInput("١٢٣")

        assertThat(state.text.toString()).isEmpty()
    }

    @Test
    fun `onComplete fires once when the last digit is typed`() {
        val completed = mutableListOf<String>()
        rule.setContent { OtpInputField(state = TextFieldState(), otpLength = 6, onComplete = { completed += it }) }

        field.performTextInput("12345")
        rule.waitForIdle()
        assertThat(completed).isEmpty()

        field.performTextInput("6")
        rule.waitForIdle()
        assertThat(completed).containsExactly("123456")
    }

    @Test
    fun `onComplete fires again after the code is edited and completed again`() {
        val completed = mutableListOf<String>()
        val state = TextFieldState()
        rule.setContent { OtpInputField(state = state, otpLength = 4, onComplete = { completed += it }) }

        field.performTextInput("1234")
        rule.waitForIdle()
        rule.runOnIdle { state.edit { replace(3, 4, "") } }
        field.performTextInput("5")
        rule.waitForIdle()

        assertThat(completed).containsExactly("1234", "1235").inOrder()
    }

    @Test
    fun `onComplete does not fire for a value that was complete from the start`() {
        val completed = mutableListOf<String>()
        rule.setContent {
            OtpInputField(state = TextFieldState("123456"), otpLength = 6, onComplete = { completed += it })
        }

        rule.waitForIdle()

        assertThat(completed).isEmpty()
    }

    @Test
    fun `onComplete does not fire again after state restoration`() {
        val completed = mutableListOf<String>()
        val restorationTester = StateRestorationTester(rule)
        restorationTester.setContent {
            OtpInputField(state = rememberTextFieldState(), otpLength = 4, onComplete = { completed += it })
        }

        field.performTextInput("1234")
        rule.waitForIdle()
        restorationTester.emulateSavedInstanceStateRestore()
        rule.waitForIdle()

        assertThat(completed).containsExactly("1234")
    }

    @Test
    fun `onComplete fires when the code is filled programmatically`() {
        val completed = mutableListOf<String>()
        val state = TextFieldState()
        rule.setContent { OtpInputField(state = state, otpLength = 6, onComplete = { completed += it }) }

        rule.runOnIdle { state.fillOtp("Your code is 654321", 6) }
        rule.waitForIdle()

        assertThat(completed).containsExactly("654321")
    }

    @Test
    fun `the latest onComplete lambda is called`() {
        val calls = mutableListOf<String>()
        var label by mutableStateOf("first")
        rule.setContent {
            val current = label
            OtpInputField(state = TextFieldState(), otpLength = 2, onComplete = { calls += current })
        }

        label = "second"
        rule.waitForIdle()
        field.performTextInput("12")
        rule.waitForIdle()

        assertThat(calls).containsExactly("second")
    }

    @Test
    fun `changing otp length re-arms completion`() {
        val completed = mutableListOf<String>()
        var length by mutableIntStateOf(4)
        val state = TextFieldState()
        rule.setContent { OtpInputField(state = state, otpLength = length, onComplete = { completed += it }) }

        length = 6
        rule.waitForIdle()
        field.performTextInput("123456")
        rule.waitForIdle()

        assertThat(completed).containsExactly("123456")
    }

    @Test
    fun `disabled field is not editable`() {
        rule.setContent { OtpInputField(state = TextFieldState(), otpLength = 6, enabled = false) }

        field.assertIsNotEnabled()
    }

    @Test
    fun `field is marked as an sms one time code for autofill`() {
        rule.setContent { OtpInputField(state = TextFieldState(), otpLength = 6) }

        rule.onNode(hasSetTextAction()).assert(isOtpField)
    }

    @Test
    fun `masked field is exposed as a password`() {
        rule.setContent {
            OtpInputField(
                state = TextFieldState(),
                otpLength = 6,
                maskConfig = OtpInputDefaults.maskConfig(isMasked = true),
            )
        }

        field.assert(SemanticsMatcher.keyIsDefined(SemanticsProperties.Password))
    }

    @Test
    fun `cells are not announced separately from the field`() {
        rule.setContent { OtpInputField(state = TextFieldState("7"), otpLength = 4) }

        // The field merges its children, so without clearing them the cell text would be read too.
        field.assert(SemanticsMatcher.keyNotDefined(SemanticsProperties.Text))
        field.assert(SemanticsMatcher.expectValue(SemanticsProperties.EditableText, AnnotatedString("7")))
    }

    @Test
    fun `cursor is moved back to the end when the selection changes`() {
        val state = TextFieldState("123")
        rule.setContent { OtpInputField(state = state, otpLength = 6) }

        rule.runOnIdle { state.edit { selection = TextRange(0) } }
        rule.waitForIdle()

        assertThat(state.selection).isEqualTo(TextRange(3))
    }

    @Test
    fun `only the next empty cell is focused, and only while the field has focus`() {
        val cells = mutableMapOf<Int, OtpCellState>()
        rule.setContent {
            OtpInputField(state = TextFieldState("12"), otpLength = 4) { cell ->
                cells[cell.index] = cell
                DefaultOtpCell(state = cell)
            }
        }
        rule.waitForIdle()
        assertThat(cells.values.none { it.isFocused }).isTrue()

        field.requestFocus()
        rule.waitForIdle()

        assertThat(cells.filterValues { it.isFocused }.keys).containsExactly(2)
        assertThat(cells.getValue(1).char).isEqualTo('2')
        assertThat(cells.getValue(2).char).isNull()
    }

    @Test
    fun `value overload passes filtered text to onValueChange`() {
        val changes = mutableListOf<String>()
        var value by mutableStateOf("")
        rule.setContent {
            OtpInputField(value = value, onValueChange = {
                changes += it
                value = it
            }, otpLength = 4)
        }

        field.performTextReplacement("ab12-34-56")
        rule.waitForIdle()

        assertThat(changes).containsExactly("1234")
        assertThat(value).isEqualTo("1234")
    }

    @Test
    fun `value overload fires onComplete when the value becomes complete`() {
        val completed = mutableListOf<String>()
        var value by mutableStateOf("12")
        rule.setContent {
            OtpInputField(
                value = value,
                onValueChange = { value = it },
                otpLength = 4,
                onComplete = { completed += it },
            )
        }

        value = "1234"
        rule.waitForIdle()

        assertThat(completed).containsExactly("1234")
    }
}
