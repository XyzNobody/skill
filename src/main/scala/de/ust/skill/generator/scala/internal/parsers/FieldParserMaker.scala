/*  ___ _  ___ _ _                                                            *\
** / __| |/ (_) | |       The SKilL Generator                                 **
** \__ \ ' <| | | |__     (c) 2013 University of Stuttgart                    **
** |___/_|\_\_|_|____|    see LICENSE                                         **
\*                                                                            */
package de.ust.skill.generator.scala.internal.parsers

import java.io.PrintWriter
import de.ust.skill.generator.scala.GeneralOutputMaker

trait FieldParserMaker extends GeneralOutputMaker {
  override def make {
    super.make
    val out = open("internal/parsers/FieldParser.scala")
    //package & imports
    out.write(s"""package ${packagePrefix}internal.parsers

import java.nio.ByteBuffer

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import scala.collection.mutable.LinkedList

import ${packagePrefix}api.SkillType
import ${packagePrefix}internal._
import ${packagePrefix}internal.pool.KnownPool
""")

    //the body itself is always the same
    copyFromTemplate(out, "FieldParser.scala.template")

    //class prefix
    out.close()
  }
}
