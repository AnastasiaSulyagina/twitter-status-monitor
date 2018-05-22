package twitter_status_monitor.server.json

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import akka.http.scaladsl.model.ContentTypeRange
import akka.http.scaladsl.model.MediaTypes.`application/json`
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import akka.util.ByteString
import tethys._
import tethys.jackson._

import scala.reflect.ClassTag

trait TethysSupport {
  lazy val unmarshallerContentTypes: Seq[ContentTypeRange] = List(`application/json`)

  implicit def unmarshaller[A](implicit reader: JsonReader[A]): FromEntityUnmarshaller[A] = {
    Unmarshaller.byteStringUnmarshaller
      .forContentTypes(unmarshallerContentTypes: _*)
      .mapWithCharset {
        case (ByteString.empty, _) => throw Unmarshaller.NoContentException
        case (data, charset) => data.decodeString(charset.nioCharset.name).jsonAs[A].fold(throw _, identity)
      }
  }

  implicit def marshaller[A](implicit ct: ClassTag[A], writer: JsonWriter[A]): ToEntityMarshaller[A] = {
    Marshaller.stringMarshaller(`application/json`).compose(_.asJson)
  }
}