import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.kotlin.serialization)
}

group = "puzzle.core"
version = "0.0.0"

kotlin {
	macosArm64 {
		binaries {
			executable {
				entryPoint = "puzzle.core.main"
			}
		}
	}
	jvm("desktop") {
		compilerOptions {
			jvmTarget = JvmTarget.JVM_25
			
		}
	}
	
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.bundles.puzzle.core)
			}
		}
		all {
			languageSettings {
				enableLanguageFeature("ContextParameters")
			}
		}
	}
}

tasks.withType<Jar>().configureEach {
	manifest {
		attributes["Main-Class"] = "puzzle.core.MainKt"
	}
}