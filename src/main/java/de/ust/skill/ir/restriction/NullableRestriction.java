package de.ust.skill.ir.restriction;

import de.ust.skill.ir.Restriction;

/**
 * Allows the argument field to contain NULL.
 * 
 * @author Timm Felden
 */
final public class NullableRestriction extends Restriction {

	@Override
	public String getName() {
		return "nullable";
	}

	@Override
	public String toString() {
		return "@nullable";
	}

}
