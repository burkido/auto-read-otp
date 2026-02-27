package com.burkido.convention

import org.gradle.api.provider.Property

interface LibraryPublishExtension {
    val groupId: Property<String>
    val artifactId: Property<String>
    val version: Property<String>
}
