package com.airtribe.learntrack.util;

import com.airtribe.learntrack.entity.enums.RuleCode;

public final class RuleExplainer {
    private RuleExplainer() {
    }

    public static String explain(RuleCode ruleCode) {
        if (ruleCode == null) {
            return "No rule selected.";
        }
        return ruleCode.getMessage();
    }

    public static String explainWithCode(RuleCode ruleCode) {
        if (ruleCode == null) {
            return "LT-RULE-UNKNOWN: No rule selected.";
        }
        return ruleCode.format();
    }
}
