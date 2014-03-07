/*  ___ _  ___ _ _                                                            *\
** / __| |/ (_) | |       The SKilL Generator                                 **
** \__ \ ' <| | | |__     (c) 2013 University of Stuttgart                    **
** |___/_|\_\_|_|____|    see LICENSE                                         **
\*                                                                            */
package de.ust.skill.generator.ada.internal

import scala.collection.JavaConversions._
import de.ust.skill.ir.Declaration
import de.ust.skill.generator.ada.GeneralOutputMaker

trait StateMakerBodyMaker extends GeneralOutputMaker {
  abstract override def make {
    super.make
    val out = open(s"""${packagePrefix}-internal-state_maker.adb""")

    out.write(s"""
package body ${packagePrefix.capitalize}.Internal.State_Maker is

   procedure Create (State : access Skill_State) is
   begin
${
  var output = "";
  for (d ← IR) {
    output += s"""      declare
         Type_Name : String := "${d.getSkillName}";
         Super_Name : String := "${if (null == d.getSuperType) "" else d.getSuperType}";
         Fields : Fields_Vector.Vector;
         Storage_Pool : Storage_Pool_Vector.Vector;
         New_Type : Type_Information := new Type_Declaration'(
            Type_Size => Type_Name'Length,
            Super_Size => Super_Name'Length,
            Name => Type_Name,
            Super_Name => Super_Name,
            bpsi => 1,
            lbpsi => 1,
            Fields => Fields,
            Storage_Pool => Storage_Pool
         );
      begin
         State.Put_Type (New_Type);
      end;\r\n\r\n"""

     output += d.getFields.filter({ f ⇒ !f.isAuto && !f.isIgnored }).map({ f ⇒
       s"""      declare
         Type_Name : String := "${d.getSkillName}";
         Field_Name : String := "${f.getSkillName}";
         New_Field : Field_Information := new Field_Declaration'(
            Size => Field_Name'Length,
            Name => Field_Name,
            F_Type => ${if (f.isConstant) mapTypeToId(f.getType) - 7 else mapTypeToId(f.getType)},
            Constant_Value => ${f.constantValue}
         );
      begin
         State.Put_Field (Type_Name, New_Field);
      end;\r\n\r\n"""
     }).mkString("")
  }
  output.stripSuffix("\r\n\r\n")
}
   end Create;

end ${packagePrefix.capitalize}.Internal.State_Maker;
""")

    out.close()
  }
}
