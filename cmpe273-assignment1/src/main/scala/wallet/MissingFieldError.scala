package wallet

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.Exception
//remove if not needed
import scala.collection.JavaConversions._

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "POST parameter(s) missing")
class MissingFieldError(error: String) extends RuntimeException(error)

