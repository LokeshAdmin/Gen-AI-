# Hypothetical training example

## Hypothetical Login Defect: Password Field Has No Visible Validation Feedback When Submitted Empty

### Environment

Application: https://app.vwo.com/
Browser: Not supplied
Browser version: Not supplied
Operating system: Not supplied
Build: Not supplied
Execution time: Not supplied

### Preconditions

1. The user is logged out.
2. An active registered test account is available.
3. The login page is accessible.

### Test Data

Registered email: <registered_email>
Password: <empty>

### Steps to Reproduce

1. Open https://app.vwo.com/.
2. Enter a registered test email in the email field.
3. Leave the password field empty.
4. Submit the login form.

### Expected Result

Because password is a mandatory field, login must not succeed with an empty password. The form should provide visible feedback indicating that the password is required, or otherwise prevent submission. The exact validation message is not specified and must not be assumed.

### Actual Result

Observed: The form submits after the password is left empty, and no visible validation feedback appears. The dashboard state after submission is not supplied. Assumption: This observation alone does not confirm whether authentication succeeds; that outcome requires execution and evidence.

### Reproducibility

3 out of 3 attempts.

### Suggested Severity and rationale

Provisional severity: Medium–High. Rationale: The mandatory-password control is not providing visible feedback, which may permit or obscure an invalid submission. If testing confirms that login succeeds or protected dashboard access is granted with an empty password, severity should be raised. This is provisional until browser, build, session state, and evidence are supplied.

### Attachments

Not provided.
