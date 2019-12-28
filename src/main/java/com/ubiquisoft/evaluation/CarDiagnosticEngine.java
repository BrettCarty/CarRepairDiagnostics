package com.ubiquisoft.evaluation;

import com.ubiquisoft.evaluation.domain.Car;
import com.ubiquisoft.evaluation.domain.ConditionType;
import com.ubiquisoft.evaluation.domain.Part;
import com.ubiquisoft.evaluation.domain.PartType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CarDiagnosticEngine {

	private static final Set<ConditionType> VALID_PART_STATUS;
	static {
		VALID_PART_STATUS = new HashSet<>();
		VALID_PART_STATUS.add(ConditionType.NEW);
		VALID_PART_STATUS.add(ConditionType.GOOD);
		VALID_PART_STATUS.add(ConditionType.WORN);
	}

	public void executeDiagnostics(Car car) {
		validateDataFields(car);
		validatePartsArePresent(car);
		validatePartsCondition(car);
	}

	private void validatePartsCondition(Car car) {
		for (Part part : car.getParts()) {
			if ((part.getCondition() == null) || (!VALID_PART_STATUS.contains(part.getCondition()))) {
				printDamagedPart(part.getType(), part.getCondition());
			}
		}
	}

	private void validatePartsArePresent(Car car) {
		Map<PartType, Integer> missingParts = car.getMissingPartsMap();
		Boolean carIsMissingParts = !missingParts.isEmpty();

		if (carIsMissingParts) {
			missingParts.keySet().stream().forEach(partType -> printMissingPart(partType, missingParts.get(partType)));
			exitIfNotValid(carIsMissingParts, "Parts");
		}
	}

	private void validateDataFields(Car car) {
		boolean isDataFieldMissing = checkForAndReportMissingDataField(car);
		exitIfNotValid(isDataFieldMissing, "Data Field");
	}

	public void exitIfNotValid(Boolean itemIsMissing, String message) {
		if (itemIsMissing) {
			System.out.println(String.format("Validation on car failed for: %s", message));
			System.exit(1);
		}
	}

	private Boolean checkForAndReportMissingDataField(Car car) {
		boolean yearIsMissing = checkIsMissingAndReport(car.getYear(), "Year");
		boolean makeIsMissing = checkIsMissingAndReport(car.getMake(), "Make");
		boolean modelIsMissing = checkIsMissingAndReport(car.getModel(), "Model");

		return yearIsMissing | makeIsMissing | modelIsMissing;
	}

	private boolean checkIsMissingAndReport(String item, String identifier) {
		if ((item == null) || (item.length() < 1)) {
			printMissingDataField(identifier);
			return true;
		}

		return false;
	}

	private void printMissingDataField(String missingField) {
		if (missingField == null) throw new IllegalArgumentException("String must not be null");

		System.out.println(String.format("Missing field Detected: %s", missingField));
	}

	private void printMissingPart(PartType partType, Integer count) {
		if (partType == null) throw new IllegalArgumentException("PartType must not be null");
		if (count == null || count <= 0) throw new IllegalArgumentException("Count must be greater than 0");

		System.out.println(String.format("Missing Part(s) Detected: %s - Count: %s", partType, count));
	}

	private void printDamagedPart(PartType partType, ConditionType condition) {
		if (partType == null) throw new IllegalArgumentException("PartType must not be null");
		if (condition == null) throw new IllegalArgumentException("ConditionType must not be null");

		System.out.println(String.format("Damaged Part Detected: %s - Condition: %s", partType, condition));
	}

	public static void main(String[] args) throws JAXBException {
		// Load classpath resource
		InputStream xml = ClassLoader.getSystemResourceAsStream("SampleCar.xml");

		// Verify resource was loaded properly
		if (xml == null) {
			System.err.println("An error occurred attempting to load SampleCar.xml");

			System.exit(1);
		}

		// Build JAXBContext for converting XML into an Object
		JAXBContext context = JAXBContext.newInstance(Car.class, Part.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		Car car = (Car) unmarshaller.unmarshal(xml);

		// Build new Diagnostics Engine and execute on deserialized car object.

		CarDiagnosticEngine diagnosticEngine = new CarDiagnosticEngine();

		diagnosticEngine.executeDiagnostics(car);

	}

}
