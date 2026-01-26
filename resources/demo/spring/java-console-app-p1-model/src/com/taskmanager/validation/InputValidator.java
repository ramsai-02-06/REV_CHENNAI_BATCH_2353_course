package com.taskmanager.validation;

import com.taskmanager.config.ConfigLoader;
import com.taskmanager.exception.ValidationException;
import com.taskmanager.exception.ValidationException.ValidationError;
import com.taskmanager.model.TaskInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Input validation using REGEX patterns and business rules.
 *
 * This class demonstrates validation that can later be replaced by:
 * - Bean Validation (JSR-380) annotations like @NotNull, @Size, @Pattern
 * - Spring's @Valid and @Validated annotations
 * - Custom validators with @Constraint
 *
 * REGEX PATTERNS EXPLAINED:
 * - Title: Must start with alphanumeric, can contain letters, numbers, spaces, and common punctuation
 * - Description: More permissive, allows most printable characters
 * - ID: Must be a positive integer
 *
 * FUTURE AOP ENHANCEMENT:
 * Validation could be applied via aspects:
 * - @Before advice on service methods to validate inputs
 * - @Around advice to transform/sanitize inputs
 */
public class InputValidator {
    private static final Logger logger = LogManager.getLogger(InputValidator.class);

    // REGEX Patterns
    private final Pattern titlePattern;
    private final Pattern idPattern;
    private final Pattern statusPattern;

    // Validation limits
    private final int titleMinLength;
    private final int titleMaxLength;
    private final int descriptionMaxLength;

    // Default patterns
    private static final String DEFAULT_TITLE_PATTERN = "^[a-zA-Z0-9][a-zA-Z0-9\\s\\-_:,.!?()]*$";
    private static final String ID_PATTERN = "^[1-9]\\d*$";
    private static final String STATUS_PATTERN = "^(PENDING|IN_PROGRESS|COMPLETED|CANCELLED)$";

    public InputValidator() {
        this(null);
    }

    public InputValidator(ConfigLoader configLoader) {
        // Load validation settings from config or use defaults
        if (configLoader != null) {
            this.titleMinLength = configLoader.getIntProperty("validation.title.minLength", 3);
            this.titleMaxLength = configLoader.getIntProperty("validation.title.maxLength", 100);
            this.descriptionMaxLength = configLoader.getIntProperty("validation.description.maxLength", 500);
            String titlePatternStr = configLoader.getProperty("validation.title.pattern", DEFAULT_TITLE_PATTERN);
            this.titlePattern = Pattern.compile(titlePatternStr);
        } else {
            this.titleMinLength = 3;
            this.titleMaxLength = 100;
            this.descriptionMaxLength = 500;
            this.titlePattern = Pattern.compile(DEFAULT_TITLE_PATTERN);
        }

        this.idPattern = Pattern.compile(ID_PATTERN);
        this.statusPattern = Pattern.compile(STATUS_PATTERN, Pattern.CASE_INSENSITIVE);

        logger.debug("InputValidator initialized - titleMin: {}, titleMax: {}, descMax: {}",
                titleMinLength, titleMaxLength, descriptionMaxLength);
    }

    /**
     * Validate a TaskInput for creating a new task.
     * Throws ValidationException if validation fails.
     */
    public void validateForCreate(TaskInput input) {
        logger.debug("Validating TaskInput for create: {}", input);

        List<ValidationError> errors = new ArrayList<>();

        // Validate title (required)
        validateTitle(input.getTitle(), errors, true);

        // Validate description (optional)
        validateDescription(input.getDescription(), errors);

        if (!errors.isEmpty()) {
            logger.warn("Validation failed with {} errors", errors.size());
            throw new ValidationException(errors);
        }

        logger.debug("Validation passed for create");
    }

    /**
     * Validate a TaskInput for updating an existing task.
     */
    public void validateForUpdate(TaskInput input) {
        logger.debug("Validating TaskInput for update: {}", input);

        List<ValidationError> errors = new ArrayList<>();

        // Validate ID (required for update)
        validateId(input.getId(), errors);

        // Validate title (optional for update, but must be valid if provided)
        if (input.getTitle() != null && !input.getTitle().trim().isEmpty()) {
            validateTitle(input.getTitle(), errors, false);
        }

        // Validate description (optional)
        if (input.getDescription() != null) {
            validateDescription(input.getDescription(), errors);
        }

        // Validate status (optional, but must be valid if provided)
        if (input.getStatus() != null && !input.getStatus().trim().isEmpty()) {
            validateStatus(input.getStatus(), errors);
        }

        if (!errors.isEmpty()) {
            logger.warn("Validation failed with {} errors", errors.size());
            throw new ValidationException(errors);
        }

        logger.debug("Validation passed for update");
    }

    /**
     * Validate task title using REGEX and length constraints.
     *
     * REGEX: ^[a-zA-Z0-9][a-zA-Z0-9\s\-_:,.!?()]*$
     * - Must start with letter or number
     * - Can contain: letters, numbers, spaces, hyphens, underscores, colons, commas, periods, punctuation
     * - Prevents: SQL injection attempts, XSS, control characters
     */
    public void validateTitle(String title, List<ValidationError> errors, boolean required) {
        if (title == null || title.trim().isEmpty()) {
            if (required) {
                errors.add(new ValidationError("title", "Title is required"));
                logger.debug("Title validation failed: empty or null");
            }
            return;
        }

        String trimmedTitle = title.trim();

        // Length validation
        if (trimmedTitle.length() < titleMinLength) {
            errors.add(new ValidationError("title",
                    String.format("Title must be at least %d characters", titleMinLength)));
            logger.debug("Title validation failed: too short ({})", trimmedTitle.length());
        }

        if (trimmedTitle.length() > titleMaxLength) {
            errors.add(new ValidationError("title",
                    String.format("Title must not exceed %d characters", titleMaxLength)));
            logger.debug("Title validation failed: too long ({})", trimmedTitle.length());
        }

        // REGEX pattern validation
        if (!titlePattern.matcher(trimmedTitle).matches()) {
            errors.add(new ValidationError("title",
                    "Title must start with a letter or number and can only contain " +
                    "letters, numbers, spaces, and common punctuation (- _ : , . ! ? ( ))"));
            logger.debug("Title validation failed: pattern mismatch for '{}'", trimmedTitle);
        }
    }

    /**
     * Validate description length.
     */
    public void validateDescription(String description, List<ValidationError> errors) {
        if (description != null && description.length() > descriptionMaxLength) {
            errors.add(new ValidationError("description",
                    String.format("Description must not exceed %d characters", descriptionMaxLength)));
            logger.debug("Description validation failed: too long ({})", description.length());
        }
    }

    /**
     * Validate task ID.
     *
     * REGEX: ^[1-9]\d*$
     * - Must be a positive integer (no leading zeros, no negatives)
     */
    public void validateId(Long id, List<ValidationError> errors) {
        if (id == null) {
            errors.add(new ValidationError("id", "Task ID is required"));
            logger.debug("ID validation failed: null");
            return;
        }

        if (id <= 0) {
            errors.add(new ValidationError("id", "Task ID must be a positive number"));
            logger.debug("ID validation failed: non-positive ({})", id);
        }
    }

    /**
     * Validate ID from string input (for UI parsing).
     */
    public Long validateAndParseId(String idStr) {
        if (idStr == null || idStr.trim().isEmpty()) {
            throw new ValidationException("id", "Task ID is required");
        }

        String trimmed = idStr.trim();

        if (!idPattern.matcher(trimmed).matches()) {
            throw new ValidationException("id", "Task ID must be a positive whole number");
        }

        try {
            long id = Long.parseLong(trimmed);
            logger.debug("Parsed ID: {}", id);
            return id;
        } catch (NumberFormatException e) {
            throw new ValidationException("id", "Task ID is too large");
        }
    }

    /**
     * Validate task status.
     *
     * REGEX: ^(PENDING|IN_PROGRESS|COMPLETED|CANCELLED)$
     * - Case insensitive
     */
    public void validateStatus(String status, List<ValidationError> errors) {
        if (status == null || status.trim().isEmpty()) {
            return; // Optional field
        }

        String normalized = status.trim().toUpperCase().replace(" ", "_");

        if (!statusPattern.matcher(normalized).matches()) {
            errors.add(new ValidationError("status",
                    "Invalid status. Must be one of: PENDING, IN_PROGRESS, COMPLETED, CANCELLED"));
            logger.debug("Status validation failed: invalid value '{}'", status);
        }
    }

    /**
     * Sanitize input string (basic XSS prevention).
     */
    public String sanitize(String input) {
        if (input == null) {
            return null;
        }
        // Basic sanitization - remove control characters and trim
        return input.replaceAll("[\\x00-\\x1F\\x7F]", "").trim();
    }
}
