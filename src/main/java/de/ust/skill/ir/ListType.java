package de.ust.skill.ir;

/**
 * @author Timm Felden
 */
public class ListType extends ContainerType implements SingleBaseTypeContainer {
	private final Type baseType;

	public static Type make(TypeContext tc, Type baseType) {
		return tc.unifyType(new ListType(baseType));
	}

	private ListType(Type baseType) {
		this.baseType = baseType;
	}

	@Override
	public Type getBaseType() {
		return baseType;
	}

	@Override
	public String getSkillName() {
		return "list<" + baseType.getSkillName() + ">";
	}
}
