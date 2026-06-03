package com.example.hxds.rule.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "根据ID查询费用规则的表单")
@Data
public class SearchChargeRuleByIdForm {
    @NotNull(message = "ruleId不为空")
    @Min(value = 1L, message = "ruleId不能小于1")
    @Schema(description = "规则ID")
    private Long ruleId;

    public static String ALLATORIxDEMO(String a) {
        int n = a.length();
        int n2 = n - 1;
        char[] cArray = new char[n];
        int n3 = 2 << 3 ^ 4;
        int cfr_ignored_0 = 3 << 3 ^ (3 ^ 5);
        int n4 = n2;
        int n5 = (2 ^ 5) << 3 ^ 5;
        while (n4 >= 0) {
            int n6 = n2--;
            cArray[n6] = (char)(a.charAt(n6) ^ n5);
            if (n2 < 0) break;
            int n7 = n2--;
            cArray[n7] = (char)(a.charAt(n7) ^ n3);
            n4 = n2;
        }
        return new String(cArray);
    }
}

 