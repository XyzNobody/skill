package de.ust.skill.ir;

/**
 * @author Timm Felden
 */
public class SetType extends ContainerType implements SingleBaseTypeContainer {
	private final Type baseType;

	public static Type make(TypeContext tc, Type baseType) {
		return tc.unifyType(new SetType(baseType));
	}

	private SetType(Type baseType) {
		this.baseType = baseType;
	}

	@Override
	public Type getBaseType() {
		return baseType;
	}

	@Override
	public String getSkillName() {
		return "set<" + baseType.getSkillName() + ">";
	}
}
