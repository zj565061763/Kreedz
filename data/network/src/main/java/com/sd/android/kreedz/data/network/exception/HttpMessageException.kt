package com.sd.android.kreedz.data.network.exception

import java.io.IOException

class HttpMessageException(
  override val message: String,
) : IOException()