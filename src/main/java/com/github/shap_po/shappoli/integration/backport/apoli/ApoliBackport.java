package com.github.shap_po.shappoli.integration.backport.apoli;

import com.github.shap_po.shappoli.integration.backport.apoli.power.factory.condition.BiEntityConditions;

/**
 * Backport class for Apoli.
 * Code should be 1:1 with the original mod, except for package names and some tweaks.
 * <p>
 * All credit goes to the original mod authors & maintainers.
 */
public class ApoliBackport {
    public static void register() {
        BiEntityConditions.register();
    }
}
