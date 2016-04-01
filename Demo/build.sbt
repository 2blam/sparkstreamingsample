name := "DemoProject"

version := "1.0"

scalaVersion := "2.10.5"

resolvers += DefaultMavenRepository


libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-core" % "1.6.0" % "provided",
	"org.apache.spark" %% "spark-sql" % "1.6.0" % "provided",	
	"org.apache.spark" %% "spark-streaming" % "1.6.0" % "provided",
	"org.apache.spark" %% "spark-streaming-twitter" % "1.6.0", 	
	"com.datastax.spark" %% "spark-cassandra-connector" % "1.5.0",
	"org.twitter4j" % "twitter4j-core" % "4.0.4",
	"org.twitter4j" % "twitter4j-stream" % "4.0.4"
)

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
    case m if m.startsWith("META-INF") => MergeStrategy.discard
    case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
    case PathList("org", "apache", xs @ _*) => MergeStrategy.first
    case PathList("org", "jboss", xs @ _*) => MergeStrategy.first
    case "about.html"  => MergeStrategy.rename
    case "reference.conf" => MergeStrategy.concat
    case _ => MergeStrategy.first
  }
}