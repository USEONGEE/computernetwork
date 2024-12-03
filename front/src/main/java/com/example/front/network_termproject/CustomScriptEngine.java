package com.example.front.network_termproject;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CustomScriptEngine {
    private final ScriptEngine scriptEngine;

    public CustomScriptEngine() {
        // Initialize the JavaScript ScriptEngine
        ScriptEngineManager manager = new ScriptEngineManager();
        this.scriptEngine = manager.getEngineByName("JavaScript");
    }

    /**
     * Evaluates a mathematical formula and returns the result.
     * @param formula The mathematical formula as a String (e.g., "1 + 2 * (3 - 4)")
     * @return The result of the formula evaluation as an integer.
     * @throws ScriptException If the formula is invalid or cannot be evaluated.
     */
    public int evaluate(String formula) throws ScriptException {
        // Replace custom operators with JavaScript-compatible ones
        String sanitizedFormula = formula.replace("×", "*").replace("÷", "/");
        Object result = scriptEngine.eval(sanitizedFormula);
        if (result instanceof Number) {
            return ((Number) result).intValue();
        } else {
            throw new ScriptException("Invalid formula result type");
        }
    }

    public static void main(String[] args) {
        // Testing the CustomScriptEngine
        CustomScriptEngine engine = new CustomScriptEngine();

        try {
            String formula = "1 + 2 * (3 - 4)"; // Example formula
            int result = engine.evaluate(formula);
            System.out.println("Result: " + result); // Should print the evaluated result
        } catch (ScriptException e) {
            System.err.println("Error evaluating formula: " + e.getMessage());
        }
    }

	public CustomScriptEngine getEngineByName(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}
