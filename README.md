# ktlint rules for the uninitiated

Custom ktlint rule set `uninitiated`. Add the JAR as a `--ruleset` when running ktlint:

```bash
ktlint --ruleset=ktlint-rules-for-the-uninitiated.jar "src/**/*.kt"
```

Enable/disable individual rules in `.editorconfig`:

```ini
ktlint_uninitiated_explicit-lambda-param = enabled
```

## Rules

- [1. `explicit-lambda-param`](#1-explicit-lambda-param)
- [2. `parentheses-before-trailing-lambda`](#2-parentheses-before-trailing-lambda)
- [3. `if-brace`](#3-if-brace)
- [4. `if-multiline`](#4-if-multiline)
- [5. `blank-line-before-return`](#5-blank-line-before-return)
- [6. `variable-name-length`](#6-variable-name-length)

---

### 1. `explicit-lambda-param`

Forbid implicit `it` in lambda expressions. Require explicit parameter names.

```kotlin
// Wrong
list.map { it.toString() }

// Correct
list.map { item -> item.toString() }
```

---

### 2. `parentheses-before-trailing-lambda`

Require parentheses before trailing lambdas. Do not allow omitting them.

```kotlin
// Wrong
list.count { it > 0 }
run { doThing() }

// Correct
list.count({ it > 0 })
run({ doThing() })
```

Note: The built-in ktlint rule `unnecessary-parentheses-before-trailing-lambda` does the opposite.
If using both rules, disable the built-in one in `.editorconfig`:

```ini
ktlint_standard_unnecessary-parentheses-before-trailing-lambda = disabled
```

---

### 3. `if-brace`

Require braces on all `if`/`else` bodies, even single-line ones.

```kotlin
// Wrong
if (bool) doThing()

// Correct
if (bool) {
    doThing()
}
```

Also flags `else` and `else if` branches without braces.

---

### 4. `if-multiline`

Require `if` statements to span multiple lines.

```kotlin
// Wrong
if (bool) { doThing() }

// Wrong
if (x) { a() } else { b() }

// Correct
if (bool) {
    doThing()
}

// Correct
if (x) {
    a()
} else {
    b()
}
```

---

### 5. `blank-line-before-return`

Require a blank line before a `return` statement when the enclosing block has
more than one statement.

```kotlin
// Wrong
fun example(value: Int): String {
    val result = doSomething(value)
    val transformed = transform(result)
    return transformed
}

// Correct
fun example(value: Int): String {
    val result = doSomething(value)
    val transformed = transform(result)

    return transformed
}

// Also fine — single statement, no blank line needed
fun simple(value: Int): String {
    return value.toString()
}
```

---

### 6. `variable-name-length`

Enforce a minimum variable name length for local variables, parameters,
destructuring entries, and class properties.

```kotlin
// Wrong
val a = 1
fun f(x: Int) {}
val (a, b) = Pair(1, 2)

// Correct
val abc = 1
fun f(param: Int) {}
val (abc, def) = Pair(1, 2)
```

By default, names shorter than 3 characters are flagged. The names `_` and `it`
are always skipped.

#### Configurable properties

| Property | Default | Description |
|---|---|---|
| `ktlint_uninitiated_min_variable_name_length` | `3` | Minimum character count |
| `ktlint_uninitiated_min_variable_name_skip_names` | `_, it` | Comma-separated names to skip |

Example `.editorconfig`:

```ini
ktlint_uninitiated_min_variable_name_length = 5
ktlint_uninitiated_min_variable_name_skip_names = _, it, id, x, y
```
