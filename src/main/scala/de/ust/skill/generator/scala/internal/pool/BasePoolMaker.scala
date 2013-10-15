/*  ___ _  ___ _ _                                                            *\
** / __| |/ (_) | |       The SKilL Generator                                 **
** \__ \ ' <| | | |__     (c) 2013 University of Stuttgart                    **
** |___/_|\_\_|_|____|    see LICENSE                                         **
\*                                                                            */
package de.ust.skill.generator.scala.internal.pool

import java.io.PrintWriter
import de.ust.skill.generator.scala.GeneralOutputMaker

trait BasePoolMaker extends GeneralOutputMaker{
  override def make {
    super.make
    val out = open("internal/pool/BasePool.scala")
    //package & imports
    out.write(s"""package ${packagePrefix}internal.pool

import ${packagePrefix}api.KnownType
import ${packagePrefix}internal.{ UserType, SerializableState, PoolIterator }

/**
 * provides common functionality for base type storage pools
 *
 * @author Timm Felden
 */
abstract class BasePool[T <: KnownType: Manifest](userType: UserType, σ: SerializableState, blockCount: Int)
    extends KnownPool[T, T](userType, blockCount) with IndexedSeq[T] {

  final override private[internal] def superPool = None

  /**
   * @return a new pool iterator
   */
  final override def iterator(): PoolIterator[T] = new PoolIterator[T](this)

  /**
   * Implements the contract of indexed seq.
   *
   * @note  that this will use an index range starting by 0 and not the ID range, which starts by 1.
   * @note the implementation makes use of the fact, that there are no other pools above this pool
   */
  final override def apply(index: Int) = getByID(index + 1L)
  /**
   * Implements the contract of indexed seq.
   *
   * @note the implementation makes use of the fact, that there are no other pools above this pool
   */
  final override def length: Int = staticSize.toInt

  /**
   * We are the base pool.
   */
  final override def basePool = this

  /**
   * the base type data store
   */
  private[pool] var data = new Array[T](userType.instanceCount.toInt)

  /**
   * construct instances of the pool in post-order, i.e. bottom-up
   */
  final override def constructPool() {
    // construct data in a bottom up order
    subPools.collect { case p: KnownPool[_, _] ⇒ p }.foreach(_.constructPool)
    userType.blockInfos.values.foreach({ b ⇒
      for (i ← b.bpsi - 1 until b.bpsi + b.count - 1)
        if (null == data(i.toInt)) {
          staticData += 1
          data(i.toInt) = newInstance
        }
    })
  }

  /**
   * returns instances directly from the data store
   *
   * @note base pool data access can not fail, because this would yeald an arary store exception at an earlier stage
   */
  final def getByID(index: Long): T = data(index.toInt - 1)
}
""")

    //class prefix
    out.close()
  }
}
