package com.ubiquisoft.evaluation.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Car {

	private String year;
	private String make;
	private String model;

	private List<Part> parts;

	public Map<PartType, Integer> getMissingPartsMap() {
		Map<PartType, Integer> missingParts = new HashMap<>();

		checkForMissingPartAndAddIfMissing(PartType.ENGINE, 1, missingParts);
		checkForMissingPartAndAddIfMissing(PartType.ELECTRICAL, 1, missingParts);
		checkForMissingPartAndAddIfMissing(PartType.FUEL_FILTER, 1, missingParts);
		checkForMissingPartAndAddIfMissing(PartType.OIL_FILTER, 1, missingParts);
		checkForMissingPartAndAddIfMissing(PartType.TIRE, 4, missingParts);

		return missingParts;
	}

	private void checkForMissingPartAndAddIfMissing(PartType partType, Integer expectedCount, Map<PartType, Integer> missingParts) {
		Integer partCount = partCount(partType);
		if (partCount < expectedCount) {
			missingParts.put(partType, expectedCount - partCount);
		}
	}

	private Integer partCount(PartType partType) {
		if (parts == null) {
			return 0;
		}
		return (int)parts.stream().filter(part -> part.getType().equals(partType)).count();
	}

	@Override
	public String toString() {
		return "Car{" +
				       "year='" + year + '\'' +
				       ", make='" + make + '\'' +
				       ", model='" + model + '\'' +
				       ", parts=" + parts +
				       '}';
	}

	/* --------------------------------------------------------------------------------------------------------------- */
	/*  Getters and Setters *///region
	/* --------------------------------------------------------------------------------------------------------------- */

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public List<Part> getParts() {
		return parts;
	}

	public void setParts(List<Part> parts) {
		this.parts = parts;
	}

	/* --------------------------------------------------------------------------------------------------------------- */
	/*  Getters and Setters End *///endregion
	/* --------------------------------------------------------------------------------------------------------------- */

}
