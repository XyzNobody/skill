package de.ust.skill.ir;

import java.util.Set;

/**
 * @see SKilL §5.2
 * @author Timm Felden
 */
public enum Hint {
	// note: names need to be lowercase, because this enum will be accessed
	// using the valueOf method
	access, modification, unique, pure, distributed, lazy, monotone, readonly, ignore;

	/**
	 * checks that the argument declaration has no illegal arguments
	 * 
	 * @throws ParseException
	 *             if there is any illegal usage of a hint
	 */
	static void checkDeclaration(final Declaration d, Set<Hint> hints) throws ParseException {
		if (hints.contains(access))
			throw new ParseException("Illegal hint !access on type declaration " + d);
		if (hints.contains(modification))
			throw new ParseException("Illegal hint !modification on type declaration " + d);

		if (hints.contains(distributed))
			throw new ParseException("Illegal hint !distributed on type declaration " + d);
		if (hints.contains(lazy))
			throw new ParseException("Illegal hint !lazy on type declaration " + d);

		if (hints.contains(monotone) && d != d.getBaseType())
			throw new ParseException(
					"The hint !monotone can only be used on base types, because it is inherited anyway: " + d);
		if (hints.contains(readonly) && d != d.getBaseType())
			throw new ParseException(
					"The hint !readOnly can only be used on base types, because it is inherited anyway: " + d);
	}

	/**
	 * checks that the argument field has legal hints only
	 * 
	 * @throws ParseException
	 *             if there is any illegal usage of a hint
	 */
	static void checkField(Field field, Set<Hint> hints) throws ParseException {
		if (hints.contains(access) && !(field.getType() instanceof ContainerType))
			throw new ParseException("The hint !access can only be used on container-typed fields: " + field);

		if (hints.contains(modification) && !(field.getType() instanceof ContainerType))
			throw new ParseException("The hint !modification can only be used on container-typed fields: " + field);

		if (hints.contains(modification) && hints.contains(access))
			throw new ParseException("The hint !modification can not be combined with !access: " + field);

		if (hints.contains(unique))
			throw new ParseException("Illegal hint !unique on field declaration " + field);

		if (hints.contains(pure))
			throw new ParseException("Illegal hint !pure on field declaration " + field);

		if (hints.contains(monotone))
			throw new ParseException("Illegal hint !monotone on field declaration " + field);

		if (hints.contains(readonly))
			throw new ParseException("Illegal hint !readOnly on field declaration " + field);
	}
}
