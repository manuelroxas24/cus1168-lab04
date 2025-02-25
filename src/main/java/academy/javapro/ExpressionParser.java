package academy.javapro;

class ExpressionParser
{
    private final String input;
    private int position;

    public ExpressionParser(String input)
    {
        this.input = input;
        this.position = 0;
    }

    // expr → expr + term
    public double parseExpression()
    {
        double result = parseTerm();
        while (position < input.length() && input.charAt(position) == '+')
        {
            position++;
            result += parseTerm();
        }
        return result;
    }

    // term → term * factor
    private double parseTerm()
    {
        double result = parseFactor();
        while (position < input.length() && input.charAt(position) == '*')
        {
            position++;
            result *= parseFactor();
        }
        return result;
    }

    // factor → ( expr )
    private double parseFactor()
    {
        if (position < input.length() && input.charAt(position) == '(')
        {
            position++;
            double result = parseExpression();
            if (position < input.length() && position >= 0 && input.charAt(position) == ')')
            {
                position++;
            }
            else
            {
                throw new RuntimeException("Mismatched parentheses");
            }
            return result;
        }
        return parseNumber();
    }

    // Parse a numeric value
    private double parseNumber()
    {
        StringBuilder number = new StringBuilder();
        boolean hasDecimal = false;
        while (position < input.length() && (Character.isDigit(input.charAt(position)) || (input.charAt(position) == '.' && !hasDecimal))) {
            if (input.charAt(position) == '.') {
                hasDecimal = true;
            }
            number.append(input.charAt(position));
            position++;
        }

        if (number.length() == 0 || number.toString().equals(".")) {
            throw new RuntimeException("Invalid number at position " + position);
        }

        return Double.parseDouble(number.toString());
    }

    public static void main(String[] args)
    {
        // Test cases
        String[] testCases =
                {
                "2 + 3 * (4 + 5)",    // Complex expression with parentheses
                "2 + 3 * 4",          // Basic arithmetic with precedence
                "(2 + 3) * 4",        // Parentheses changing precedence
                "2 * (3 + 4) * (5 + 6)", // Multiple parentheses
                "1.5 + 2.5 * 3"       // Decimal numbers
        };

        // Process each test case
        for (String expression : testCases)
        {
            System.out.println("\nTest Case: " + expression);
            try
            {
                ExpressionParser parser = new ExpressionParser(expression.replaceAll("\\s+", "")); // Remove spaces
                double result = parser.parseExpression();
                if (parser.position < parser.input.length())
                {
                    throw new RuntimeException("Unexpected end of expression");
                }
                System.out.println("Result: " + result);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}