/*  ___ _  ___ _ _                                                            *\
** / __| |/ (_) | |       The SKilL Generator                                 **
** \__ \ ' <| | | |__     (c) 2013 University of Stuttgart                    **
** |___/_|\_\_|_|____|    see LICENSE                                         **
\*                                                                            */
package de.ust.skill.generator.scala.api

import scala.collection.JavaConversions._

import de.ust.skill.generator.scala.GeneralOutputMaker

trait SkillStateMaker extends GeneralOutputMaker {
  override def make {
    super.make
    val out = open("api/SkillState.scala")

    //package & imports
    out.write(s"""package ${packagePrefix}api

import java.nio.file.Path

import scala.collection.mutable.ArrayBuffer

import ${packagePrefix}internal.SerializableState

/**
 * The public interface to the SKilL state, which keeps track of serializable objects and provides (de)serialization
 *  capabilities.
 *
 * @author Timm Felden
 */
trait SkillState {
  /**
   * Creates a new SKilL file at target. The recommended file extension is ".sf".
   */
  def write(target: Path): Unit

  /**
   * retrieves a string from the known strings; this can cause disk access, due to the lazy nature of the implementation
   *
   * @throws ArrayOutOfBoundsException if index is not valid
   */
  def getString(index: Long): String
  /**
   * adds a string to the state
   */
  def addString(string: String): Unit
""")

    //access to declared types
    IR.foreach({ t ⇒
      val name = t.getName()
      val Name = name.capitalize
      val sName = name.toLowerCase()
      val tName = "_root_."+packagePrefix + name

      val addArgs = t.getAllFields().filter(!_.isConstant).map({ f ⇒ s"${f.getName().capitalize}: ${mapType(f.getType())}" }).mkString(", ")

      out.write(s"""
  /**
   * returns a $name iterator
   */
  def get${Name}s(): Iterator[$tName]
  /**
   * adds a new $name to the $name pool
   */
  def add$Name($addArgs): $tName
""")

    })

    // second part: reading of files
    out.write("""}

object SkillState {
  /**
   * Creates a new and empty SKilL state.
   */
  def create: SkillState = SerializableState.create

  /**
   * Reads a binary SKilL file and turns it into a SKilL state.
   */
  def read(target: Path): SkillState = SerializableState.read(target)
}""")

    out.close()
  }
}
