package adoption.ui;

import adoption.exception.AdoptionException;
import adoption.guide.CareGuide;
import adoption.guide.CostItem;
import adoption.guide.CostItem.Frequency;
import adoption.model.Animal.AnimalType;
import adoption.service.CareGuideService;

public class CareGuideMenu {
    private static final String[] OPTIONS = {
        "Responsible Ownership and Basic Care", "Estimated Expenses", "Veterinary Service Estimates",
        "Essential Supplies", "Long-term Cost Projection", "Affordable Veterinary Care", "Back"
    };

    private static final int DEFAULTYEARS = 5;
    private static final int MAXYEARS = 20;

    private final ConsoleInput input;
    private final CareGuideService careGuideService;

    public CareGuideMenu(ConsoleInput input, CareGuideService careGuideService) {
        this.input = input;
        this.careGuideService = careGuideService;
    }

    public void show() {
        AnimalType type = input.chooseOption("\nWhich kind of pet is the guide for", AnimalType.values(), null);
        showFor(type);
    }

    public void showFor(AnimalType type) {
        try {
            CareGuide guide = careGuideService.getGuide(type);
            
            boolean running = true;
            while (running) {
                System.out.println("\nResponsible Ownership and Cost Guide: " + type);
            
                for (int i = 0; i < OPTIONS.length; i++) {
                    System.out.println((i + 1) + ". " + OPTIONS[i]);
                }

                switch (input.prompt("Choose an option")) {
                    case "1" -> printBasicCare(guide);
                    case "2" -> printExpenses(guide);
                    case "3" -> printVetServices(guide);
                    case "4" -> printSupplies(guide);
                    case "5" -> printProjection(guide);
                    case "6" -> printAffordableCare(guide);
                    case "7" -> running = false;
                    default -> System.out.println("Invalid option. Please enter 1 to 7.");
                }
            } 
        } catch (AdoptionException e) {
            System.out.println("Failed: " + e.getMessage());
        }
    }

    private void printBasicCare(CareGuide guide) {
        System.out.println();
        for (String paragraph : guide.getIntroduction()) {
            System.out.println(paragraph + "\n");
        }

        for (CareGuide.Section section : guide.getGeneralSections()) {
            System.out.println("== " + section.getTitle() + " ==");
            for (String tip : section.getTips()) {
                System.out.println("  - " + tip);
            }
            System.out.println();
        }
    }

    private void printExpenses(CareGuide guide) {
        System.out.println("\nEstimated Expenses of Owning a " + guide.getAnimalType());
        System.out.println("Basis: " + guide.getCostBasis());

        if (guide.hasInitialCosts()) {
            System.out.println("\nInitial Expenses (one-time):");
            printItems(guide, Frequency.ONE_TIME);
            System.out.println("  Estimated total: " + CostItem.formatRange(
                    guide.getInitialMin(), guide.getInitialMax(), guide.hasOpenEndedInitialCost()));
        }

        System.out.println("\nMonthly Expenses:");
        printItems(guide, Frequency.MONTHLY);
        System.out.println("  Estimated monthly total: "
                + CostItem.formatRange(guide.getMonthlyMin(), guide.getMonthlyMax(), false));
        System.out.println("  Estimated annual total:  "
                + CostItem.formatRange(guide.getProjectedMin(1), guide.getProjectedMax(1), false));

        System.out.println();
        for (String note : guide.getCostNotes()) {
            System.out.println("Note: " + note);
        }
    }

    private void printItems(CareGuide guide, Frequency frequency) {
        for (CostItem item : guide.getCostItems()) {
            if (item.getFrequency() == frequency) {
                System.out.println("  - " + item.getName() + ": " + item.getRangeText());
            }
        }
    }

    private void printVetServices(CareGuide guide) {
        System.out.println("\nEstimated Veterinary and Healthcare Expenses");
        System.out.println("Consultations, vaccinations, deworming, preventive treatments, and emergency services.\n");
        for (CostItem item : guide.getVetServices()) {
            System.out.println("  - " + item.getName() + ": " + item.getRangeText());
        }
        System.out.println("\nNote: " + guide.getVetPriceNote());
        System.out.println("These service prices are for reference and are not added to the totals above.");
    }

    private void printSupplies(CareGuide guide) {
        System.out.println("\nEssential Supplies for " + guide.getAnimalType() + "s");
        for (String supply : guide.getEssentialSupplies()) {
            System.out.println("  - " + supply);
        }
    }

    private void printProjection(CareGuide guide) {
        String answer = input.prompt("Project for how many years? (Enter for " + DEFAULTYEARS + ")");
        int years = DEFAULTYEARS;
        try {
            if (!answer.isBlank()) {
                years = Integer.parseInt(answer);
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a whole number of years.");
            return;
        }
        if (years < 1 || years > MAXYEARS) {
            System.out.println("Years must be between 1 and " + MAXYEARS + ".");
            return;
        }

        System.out.println("\nEstimated Long-term Cost of Owning a " + guide.getAnimalType());
        System.out.println("Based on the regular monthly expenses only. Basis: " + guide.getCostBasis());
        printLine("Monthly", guide.getMonthlyMin(), guide.getMonthlyMax());
        printLine("Annual", guide.getProjectedMin(1), guide.getProjectedMax(1));
        printLine(years + "-year projection", guide.getProjectedMin(years), guide.getProjectedMax(years));

        if (guide.hasInitialCosts()) {
            System.out.println("\nNot included above: initial expenses of "
                    + CostItem.formatRange(guide.getInitialMin(), guide.getInitialMax(),
                            guide.hasOpenEndedInitialCost()) + ".");
        }
        System.out.println("\n" + guide.getConclusion());
    }

    private void printLine(String label, double min, double max) {
        System.out.println(String.format("  %-22s %s", label, CostItem.formatRange(min, max, false)));
    }

    private void printAffordableCare(CareGuide guide) {
        System.out.println("\nAffordable veterinary services and preventive care");
        System.out.println("Local government units (LGUs), through their City or Municipal Veterinary Offices may provide free or affordable veterinary services, including:");
        for (String service : guide.getLguServices()) {
            System.out.println("  - " + service);
        }
        System.out.println();
        for (String note : guide.getAffordableCareNotes()) {
            System.out.println(note + "\n");
        }
    }
}