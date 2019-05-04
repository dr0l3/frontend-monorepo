import sbt.Keys.libraryDependencies

name := "monorepo-scalajs"

version := "0.1"

scalaVersion := "2.12.8"

resolvers += "Apollo Bintray" at "https://dl.bintray.com/apollographql/maven/"

val namespace = "com.drole"

val npmDeps = Seq(
  npmDependencies in Compile += "react" -> "16.8.6",
  npmDependencies in Compile += "react-dom" -> "16.8.6",
  npmDependencies in Compile += "react-proxy" -> "1.1.8",
  npmDependencies in Compile += "apollo-boost" -> "0.1.16",
  npmDependencies in Compile += "react-apollo" -> "2.2.2",
  npmDependencies in Compile += "graphql-tag" -> "2.9.2",
  npmDependencies in Compile += "graphql" -> "14.0.2",
  npmDevDependencies in Compile += "file-loader" -> "3.0.1",
  npmDevDependencies in Compile += "style-loader" -> "0.23.1",
  npmDevDependencies in Compile += "css-loader" -> "2.1.1",
  npmDevDependencies in Compile += "html-webpack-plugin" -> "3.2.0",
  npmDevDependencies in Compile += "copy-webpack-plugin" -> "5.0.2",
  npmDevDependencies in Compile += "webpack-merge" -> "4.2.1",
)

val scalaDeps = Seq(
  libraryDependencies += "com.apollographql" %%% "apollo-scalajs-core" % "0.7.0",
  libraryDependencies += "com.apollographql" %%% "apollo-scalajs-react" % "0.7.0",
  libraryDependencies += "me.shadaj" %%% "slinky-web" % "0.6.0",
  libraryDependencies += "me.shadaj" %%% "slinky-hot" % "0.6.0",
  libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.5" % Test,
  libraryDependencies += "com.lambdaminute" %%% "slinky-wrappers-react-router" % "0.4.1",
)

val components = project
  .in(file("components"))
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    scalacOptions += "-P:scalajs:sjsDefinedByDefault",
    addCompilerPlugin(
      "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
  )
  .settings(npmDeps ++ scalaDeps)

val apolloScalaJsPlayground = project
  .in(file("apollo-scala-js-playground"))
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    scalacOptions += "-P:scalajs:sjsDefinedByDefault",
    addCompilerPlugin(
      "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),
    version in webpack := "4.29.6",
    version in startWebpackDevServer := "3.2.1",
    webpackResources := baseDirectory.value / "webpack" * "*",
    webpackConfigFile in fastOptJS := Some(
      baseDirectory.value / "webpack" / "webpack-fastopt.config.js"),
    webpackConfigFile in fullOptJS := Some(
      baseDirectory.value / "webpack" / "webpack-opt.config.js"),
    webpackConfigFile in Test := Some(
      baseDirectory.value / "webpack" / "webpack-core.config.js"),
    webpackDevServerExtraArgs in fastOptJS := Seq("--inline", "--hot"),
    webpackBundlingMode in fastOptJS := BundlingMode.LibraryOnly(),
    requireJsDomEnv in Test := true,
    addCommandAlias("dev", ";fastOptJS::startWebpackDevServer;~fastOptJS"),
    addCommandAlias("build", "fullOptJS::webpack"),
    (sourceGenerators in Compile) += Def.task {
      import scala.sys.process._

      val out = (sourceManaged in Compile).value

      out.mkdirs()

      val graphQLScala = out / "graphql.scala"

      Seq(
        "apollo",
        "client:codegen",
        "--config",
        "apollo.config.js",
        "--target",
        "scala",
        "--namespace",
        namespace,
        graphQLScala.getAbsolutePath
      ).!

      Seq(out / "graphql.scala")
    }
  )
  .dependsOn(components)
  .settings(npmDeps ++ scalaDeps)

lazy val reactRouter =
  project
    .in(file("react-router"))
    .enablePlugins(ScalaJSPlugin)
    .settings(
      libraryDependencies += "me.shadaj" %%% "slinky-web" % "0.6.0",
      addCompilerPlugin(
        "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
    )


val helloWorld2 = project
  .in(file("hello-world2"))
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    name := "hello-world2",
    scalaVersion := "2.12.8",
    npmDependencies in Compile += "react" -> "16.8.6",
    npmDependencies in Compile += "react-dom" -> "16.8.6",
    npmDependencies in Compile += "react-proxy" -> "1.1.8",
    npmDependencies in Compile += "react-router-dom" -> "4.2.2",
    npmDependencies in Compile += "apollo-boost" -> "0.1.16",
    npmDependencies in Compile += "react-apollo" -> "2.2.2",
    npmDependencies in Compile += "graphql-tag" -> "2.9.2",
    npmDependencies in Compile += "graphql" -> "14.0.2",
    npmDevDependencies in Compile += "file-loader" -> "3.0.1",
    npmDevDependencies in Compile += "style-loader" -> "0.23.1",
    npmDevDependencies in Compile += "css-loader" -> "2.1.1",
    npmDevDependencies in Compile += "html-webpack-plugin" -> "3.2.0",
    npmDevDependencies in Compile += "copy-webpack-plugin" -> "5.0.2",
    npmDevDependencies in Compile += "webpack-merge" -> "4.2.1",
    libraryDependencies += "me.shadaj" %%% "slinky-web" % "0.6.0",
    libraryDependencies += "me.shadaj" %%% "slinky-hot" % "0.6.0",
    libraryDependencies += "org.scalatest" %%% "scalatest" % "3.0.5" % Test,
    scalacOptions += "-P:scalajs:sjsDefinedByDefault",
    addCompilerPlugin(
      "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),
    version in webpack := "4.29.6",
    version in startWebpackDevServer := "3.2.1",
    webpackResources := baseDirectory.value / "webpack" * "*",
    webpackConfigFile in fastOptJS := Some(
      baseDirectory.value / "webpack" / "webpack-fastopt.config.js"),
    webpackConfigFile in fullOptJS := Some(
      baseDirectory.value / "webpack" / "webpack-opt.config.js"),
    webpackConfigFile in Test := Some(
      baseDirectory.value / "webpack" / "webpack-core.config.js"),
    webpackDevServerExtraArgs in fastOptJS := Seq("--inline", "--hot"),
    webpackBundlingMode in fastOptJS := BundlingMode.LibraryOnly(),
    requireJsDomEnv in Test := true,
    addCommandAlias("dev", ";fastOptJS::startWebpackDevServer;~fastOptJS"),
    addCommandAlias("build", "fullOptJS::webpack"),
    (sourceGenerators in Compile) += Def.task {
      import scala.sys.process._

      val out = (sourceManaged in Compile).value

      out.mkdirs()

      val graphQLScala = out / "graphql.scala"

      Seq(
        "apollo",
        "client:codegen",
        "--config",
        "hello-world2/apollo.config.js",
        "--target",
        "scala",
        "--namespace",
        namespace,
        graphQLScala.getAbsolutePath
      ).!

      Seq(out / "graphql.scala")
    }
  ).dependsOn(components).dependsOn(reactRouter)
