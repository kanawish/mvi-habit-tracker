Pod::Spec.new do |spec|
    spec.name                     = 'libCommon'
    spec.version                  = '1.0-SNAPSHOT'
    spec.homepage                 = 'https://github.com/kanawish/mvi-habit-tracker'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'Common library for my MVI Habit Tracker sample'
    spec.vendored_frameworks      = 'build/cocoapods/framework/libCommon.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '15.4'
                
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':libCommon',
        'PRODUCT_MODULE_NAME' => 'libCommon',
    }
                
    spec.script_phases = [
        {
            :name => 'Build libCommon',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$COCOAPODS_SKIP_KOTLIN_BUILD" ]; then
                  echo "Skipping Gradle build task invocation due to COCOAPODS_SKIP_KOTLIN_BUILD environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
                
end