import scala.util.matching.Regex

val getValidateProjectIdInUri:String = ".*/(\\d+)/.*"

val getRequestURI = "http://www.example.com/derp/12345/herp/woot"

val projectIdUriRegex = Option(getValidateProjectIdInUri).map(_.r)

val extractedProjectId = projectIdUriRegex.map(extractProjectIdFromUri(_, getRequestURI)).getOrElse(None)

def extractProjectIdFromUri(projectIdRegex: Regex, uri: String): Option[String] =
  projectIdRegex.findFirstMatchIn(uri).map(regexMatch => regexMatch.group(1))

extractProjectIdFromUri(projectIdUriRegex.get, getRequestURI)

def extractId(uri:String, regex:Option[Regex]):Option[String] = {
  regex match {
    case Some(r) => {
      r.findFirstMatchIn(uri).map(Some(_.group(1))).getOrElse(None)
    }
    case None => None
  }
}

extractId(getRequestURI, Some(getValidateProjectIdInUri.r))