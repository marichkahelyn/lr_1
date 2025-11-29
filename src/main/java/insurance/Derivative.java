package insurance;

import java.util.*;

public class Derivative {
    private List<InsurancePolicy> policies = new ArrayList<>();

    public void addPolicy(InsurancePolicy policy) {
        policies.add(policy);
    }

    public double calculateTotalValue() { //загальна сума премій
        return policies.stream().mapToDouble(InsurancePolicy::calculatePremium).sum();
    }

    public void sortByRisk(boolean ascending) {
        policies.sort(Comparator.comparingDouble(InsurancePolicy::getRisk));// у зростаючому порядку
        if (!ascending) Collections.reverse(policies);
    }

    public List<InsurancePolicy> findByParameters(double minRisk, double maxRisk, double maxObligation) {
        List<InsurancePolicy> result = new ArrayList<>(); //для збереження знайдених полісів
        for (InsurancePolicy p : policies) {
            if (p.getRisk() >= minRisk && p.getRisk() <= maxRisk && p.getObligation() <= maxObligation) {
                result.add(p);
            }
        }
        return result;
    }

    public List<InsurancePolicy> getPolicies() { //для перегляду полісів
        return policies;
    }
}
