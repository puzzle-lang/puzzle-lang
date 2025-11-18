plugins {
	alias(libs.plugins.kotlin.multiplatform)
	alias(libs.plugins.kotlin.serialization)
}

group = "puzzle.core"
version = "0.0.1"

kotlin {
	macosArm64 {
		binaries {
			executable {
				entryPoint = "puzzle.core.main"
			}
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