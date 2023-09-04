To Reproduce:

1. Ensure kotlin version is `1.9.20-Beta-205` in build.gradle.kts
2. Run `./gradlew clean :runDebugExecutableNative --rerun-tasks`

See an error in logs:
```
> Task :linkDebugExecutableNative FAILED
e: Multiple values are not allowed for plugin option androidx.compose.compiler.plugins.kotlin:sourceInformation
```

3. Run with `--debug`:
In logs we can see that our options were indeed added twice:
```
Transformed arguments = [
        konanc
        -g
        -enable-assertions
        -Xinclude=/home/username/Downloads/repr_knative_free_args/build/classes/kotlin/native/main/klib/repr_knative_free_args.klib
        -library
        /home/username/.gradle/caches/modules-2/files-2.1/org.jetbrains.compose.runtime/runtime-linuxx64/1.5.0/6b42e621ea5317945494cd0e7725d8df8d4a38da/runtime.klib
        -library
        /home/username/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlinx/kotlinx-coroutines-core-linuxx64/1.6.4/b000135c53056e0b032a7342b69a09b5bddccdcb/kotlinx-coroutines-core.klib
        -library
        /home/username/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlinx/atomicfu-linuxx64/0.17.3/ea2603c4343b2f1e779053662a6e2879842af2d9/atomicfu.klib
        -library
        /home/username/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlinx/atomicfu-linuxx64/0.17.3/dbf7ab263d8ddbeedc0376daf2646be87267276b/atomicfu-cinterop-interop.klib
        -entry
        main
        -no-endorsed-libs
        -output
        /home/username/Downloads/repr_knative_free_args/build/bin/native/debugExecutable/repr_knative_free_args.kexe
        -produce
        program
        -target
        linux_x64
        -Xmulti-platform
        -Xplugin=/home/username/.gradle/caches/modules-2/files-2.1/org.jetbrains.compose.compiler/compiler/0.0.0-1.9.20-Beta-205/3ea94a4e442235f353ccd58052cc770e866c2818/compiler-0.0.0-1.9.20-Beta-205.jar
        -P
        plugin:androidx.compose.compiler.plugins.kotlin:sourceInformation=true
        -Xplugin=/home/username/.gradle/caches/modules-2/files-2.1/org.jetbrains.compose.compiler/compiler/0.0.0-1.9.20-Beta-205/3ea94a4e442235f353ccd58052cc770e866c2818/compiler-0.0.0-1.9.20-Beta-205.jar
        -P
        plugin:androidx.compose.compiler.plugins.kotlin:sourceInformation=true
        -Xexternal-dependencies=/tmp/kotlin-native-external-dependencies4005856749820098911.deps
        -Xauto-cache-from=/home/username/.gradle
        -Xbackend-threads=4
]

```

4. Switch to kotlin `1.9.10` and run the same tasks. It works and runs.
5. K/js node app runs with both kotlin versions.