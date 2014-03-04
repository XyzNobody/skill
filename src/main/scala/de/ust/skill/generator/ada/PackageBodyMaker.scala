/*  ___ _  ___ _ _                                                            *\
** / __| |/ (_) | |       The SKilL Generator                                 **
** \__ \ ' <| | | |__     (c) 2013 University of Stuttgart                    **
** |___/_|\_\_|_|____|    see LICENSE                                         **
\*                                                                            */
package de.ust.skill.generator.ada

import java.io.PrintWriter
import scala.collection.JavaConversions._

trait PackageBodyMaker extends GeneralOutputMaker {
  abstract override def make {
    super.make
    val out = open(s"""${packagePrefix}.adb""")

    out.write(s"""
package body ${packagePrefix.capitalize} is

${
  var output = "";
  for (d ← IR) {
    d.getAllFields.filter { f ⇒ !f.isIgnored }.foreach({ f ⇒
      if (f.isConstant) {
        output += s"""   function Get_${f.getSkillName.capitalize} (Object : ${d.getName}_Type) return ${mapType(f.getType)} is (${f.constantValue});\r\n\r\n"""
      }
      else {
        output += s"""   function Get_${f.getSkillName.capitalize} (Object : ${d.getName}_Type) return ${mapType(f.getType)} is (Object.${f.getSkillName});\r\n"""
        output += s"""   procedure Set_${f.getSkillName.capitalize} (Object : in out ${d.getName}_Type; Value : ${mapType(f.getType)}) is
   begin
      Object.${f.getSkillName} := Value;
   end Set_${f.getSkillName.capitalize};\r\n\r\n"""
      }
    })
  }
  output.stripSuffix("\r\n\r\n");
}

   protected body Skill_State is

      -------------------
      --  STRING POOL  --
      -------------------
      function Get_String (Index : Positive) return String is
         (String_Pool.Element (Index));

      function Get_String (Index : Long) return String is
         (Get_String (Positive (Index)));

      function Get_String_Index (Value : String) return Natural is
         (String_Pool.Reverse_Find_Index (Value));

      function String_Pool_Size return Natural is
         (Natural (String_Pool.Length));

      procedure Put_String (Value : String) is
      begin
         String_Pool.Append (Value);
      end Put_String;

      --------------------
      --  STORAGE_POOL  --
      --------------------
      function Get_Object (Type_Name : String; Position : Positive) return Skill_Type_Access is
         (Get_Type (Type_Name).Storage_Pool.Element (Position));

      function Storage_Pool_Size (Type_Name : String) return Natural is
         (Natural (Get_Type (Type_Name).Storage_Pool.Length));

      procedure Put_Object (Type_Name : String; New_Object : Skill_Type_Access) is
      begin
         Get_Type (Type_Name).Storage_Pool.Append (New_Object);
      end Put_Object;

      procedure Replace_Object (Type_Name : String; Position : Positive; New_Object : Skill_Type_Access) is
      begin
         Get_Type (Type_Name).Storage_Pool.Replace_Element (Position, New_Object);
      end Replace_Object;

      --------------------------
      --  FIELD DECLARATIONS  --
      --------------------------
      function Known_Fields (Name : String) return Long is
         (Long (Types.Element (Name).Fields.Length));

      function Get_Field (Type_Name : String; Position : Long) return Field_Information is
         X : Type_Information := Get_Type (Type_Name);
      begin
         return X.Fields.Element (Positive (Position));
      end Get_Field;

      procedure Put_Field (Type_Name : String; New_Field : Field_Information) is
         Type_Declaration : Type_Information := Types.Element (Type_Name);
      begin
         Type_Declaration.Fields.Append (New_Field);
      end Put_Field;

      --------------------------
      --  TYPES DECLARATIONS  --
      --------------------------
      function Has_Type (Name : String) return Boolean is
      begin
         return Types.Contains (Name);
      end Has_Type;

      function Get_Type (Name : String) return Type_Information is
         Skill_Unexcepted_Type_Name : exception;
      begin
         if not Has_Type (Name) then
            raise Skill_Unexcepted_Type_Name;
         end if;
         return Types.Element (Name);
      end Get_Type;

      procedure Put_Type (New_Type : Type_Information) is
      begin
         Types.Insert (New_Type.Name, New_Type);
      end Put_Type;

   end Skill_State;

end ${packagePrefix.capitalize};
""")

    out.close()
  }
}
