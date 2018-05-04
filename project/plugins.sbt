libraryDependencies ++= Seq(
  "org.scala-sbt" %% "scripted-plugin" % "1.1.2"// sbtVersion.value
)

resolvers += Resolver.typesafeIvyRepo("releases")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.7")