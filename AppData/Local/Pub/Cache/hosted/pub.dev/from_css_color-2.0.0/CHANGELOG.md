## 2.0.0

* Migrate to null-safety.
* Remove fromCSSColor()/isCSSColor(), which was deprecated since 1.2.0 in favor of fromCssColor()/isCssColor() to meet the [effective Dart style guide](https://dart.dev/guides/language/effective-dart/style#do-capitalize-acronyms-and-abbreviations-longer-than-two-letters-like-words).

## 1.2.0

* Add `isCssColor()` function to check color string format correctness.

## 1.1.0

* Add `toCssColor()` extension method to convert Color instance to CSS color string.

## 1.0.0

* Add support for 4 (#rgba) and 8 (#rrggbbaa) digit hex colors.
* Update versioning to stable after 3 months of usage in production (no breaking changes expected).

## 0.1.1

* Update readme and file formatting.

## 0.1.0

* Support for 'transparent' color keyword.
* Support for hsl/hsla colors.

## 0.0.1

* Support for 3 and 6 digit hex colors.
* Support for rgb/rgba colors.
* Support for X11 keywords.
