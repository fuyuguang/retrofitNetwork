package com.mvvm.comm

public inline fun String?.whatIfNotNullOrEmpty(
  whatIf: String.() -> Unit
): String? {

  this.whatIfNotNullOrEmpty(
    whatIf = whatIf,
    whatIfNot = { }
  )
  return this
}

public inline fun String?.whatIfNotNullOrEmpty(
  whatIf: String.() -> Unit,
  whatIfNot: () -> Unit
): String? {
  if (!this.isNullOrEmpty()) {
    whatIf()
  } else {
    whatIfNot()
  }
  return this
}
