# Test Plan – VWO Login Module

**Test Plan ID:** TP-VWO-LOGIN-001  
**Title:** Functional Test Plan for VWO Login Module  
**Application:** https://app.vwo.com/  
**Prepared for:** QA review and editing  
**Requirement status:** Approved requirements supplied in the STAR prompt

## Table of Contents

1. [Test Plan](#1-test-plan)
2. [Test Strategy](#2-test-strategy)
3. [Test Schedule](#3-test-schedule)
4. [Test Deliverables](#4-test-deliverables)
5. [Entry and Exit Criteria](#5-entry-and-exit-criteria)
6. [Test Execution](#6-test-execution)
7. [Test Closure](#7-test-closure)
8. [Tools](#8-tools)
9. [Risks and Mitigations](#9-risks-and-mitigations)
10. [Approvals](#10-approvals)

## 1. Test Plan

### Objective

Verify that the login module for registered users:

- Accepts valid credentials and opens the authenticated dashboard.
- Rejects invalid credentials without granting access.
- Requires both email and password.
- Displays the specified invalid-login error message.
- Prevents unauthenticated access to protected dashboard functionality.

### Scope

#### In Scope

- Login form fields and their required-value behavior.
- Positive login using valid credentials for a registered user.
- Negative login using invalid email, invalid password, and unknown credentials.
- Empty email and/or password submissions.
- Login result and navigation to the authenticated dashboard.
- Verification that an invalid login does not grant dashboard access.
- UI behavior supported by the supplied requirements and observable test evidence.
- Boundary scenarios only where approved requirements or current field behavior provide a valid basis.

#### Out of Scope

- Password creation, complexity, reset, expiration, or strength rules.
- Account registration and account provisioning.
- Session timeout and remember-me behavior unless separately clarified and approved.
- Social login, single sign-on, multi-factor authentication, and CAPTCHA unless present and added to the scope.
- IP and location policy validation beyond the behavior named in the supplied invalid-login message.
- Security penetration testing, load testing, and vulnerability assessment.
- Backend/database testing and external identity-provider testing outside the application UI.
- Testing or assuming third-party service availability.

**Recommendation:** Confirm the scope of authentication features visible on the login page before execution.

### Test Environments

| Item | Requirement | Status / Clarification |
|---|---|---|
| Application URL | https://app.vwo.com/ | Approved in task context |
| Test environment | An approved QA/UAT login environment or the public application with authorized test accounts | Requires clarification |
| Registered test account | Valid email and password belonging to an approved test user | Test credentials must be provided securely |
| Invalid test account | Known-invalid email/password that cannot accidentally match a real account | Recommended test data |
| Test browser | At least one supported modern desktop browser | Browser versions require confirmation |
| Secondary browser | One additional supported desktop browser | Recommended where environment permits |
| Mobile browser | One supported mobile viewport or browser | Recommended only if login UI is in scope for mobile |
| Network | Stable internet connection with access to the application | Required |
| Access role | Test account with access to an authenticated dashboard | Dashboard behavior requires confirmation |

**Defect reporting procedure:** Log reproducible defects with environment, browser, test data classification, steps, expected and actual results, screenshots or video, network trace if relevant, severity, priority, and timestamp. Protect passwords and sensitive account information by recording masked values or references to a secure test-data repository.

## 2. Test Strategy

### Test Approach

Use manual functional testing for the login workflow, supplemented by browser developer tools for verification of navigation and network outcomes where permitted. Execute positive scenarios first, then negative and boundary scenarios. Verify both the visible login result and whether protected dashboard content or an authenticated session becomes available after login.

### Test Types

| Test type | Coverage |
|---|---|
| Positive | Registered credentials, dashboard navigation, and authenticated access |
| Negative | Invalid email, invalid password, unknown account, and invalid credential combinations |
| Required-field | Missing email, missing password, and both fields missing |
| Boundary | Only observable field lengths or formats that can be tested from an approved requirement; otherwise mark for clarification |
| Regression | Confirm that login continues to work after changes to the login or authentication module |
| Exploratory | Confirm button states and navigation behavior using the supplied requirements, without assuming undocumented rules |

**No password rules, exact session timeout, account lockout, maximum field lengths, or CAPTCHA behavior are to be assumed.**

### Test Scenarios

| ID | Scenario / Type | Precondition / Test Data | Steps | Expected Result |
|---|---|---|---|---|
| LGN-001 | Valid login – Positive | Authorized registered test account | Open login page; enter valid email and valid password; submit | Authentication succeeds and the authenticated dashboard opens; no invalid-login error is shown. |
| LGN-002 | Valid email, invalid password – Negative | Registered email and known-invalid password | Enter the registered email and invalid password; submit | Access is not granted. The invalid-login message is “Your email, password, IP address or location did not match” unless the approved UI is updated and approved requirements change. |
| LGN-003 | Invalid email, valid password – Negative | Known-invalid email and a password known to be valid for a different test account, if available | Enter the values; submit | Access is not granted and the specified invalid-login message is shown. |
| LGN-004 | Both credentials invalid – Negative | Known-invalid email and known-invalid password | Enter both values; submit | Access is not granted and the specified invalid-login message is shown. |
| LGN-005 | Empty email – Negative/Required | Password is populated; email is empty | Submit the form | Login is not completed. Email is treated as mandatory. The exact field-level behavior is **Requires clarification**. |
| LGN-006 | Empty password – Negative/Required | Email is populated; password is empty | Submit the form | Login is not completed. Password is treated as mandatory. The exact field-level behavior is **Requires clarification**. |
| LGN-007 | Both fields empty – Negative/Required | Both fields are empty | Submit the form | Login is not completed. Email and password are each treated as mandatory. Exact field-level behavior is **Requires clarification**. |
| LGN-008 | Unauthorized dashboard access – Negative | Log out or use a fresh unauthenticated browser session | Attempt to open a protected dashboard URL directly | Dashboard access is not available without successful authentication. Exact redirect destination is **Requires clarification**. |
| LGN-009 | Email format boundary – Boundary | Candidate: invalid formatting, such as missing “@” or domain | Enter an invalidly formatted email and submit | Credentials do not grant access. The exact format validation message is **Requires clarification**. Password rules and error text must not be assumed. |
| LGN-010 | Unrelated special characters – Boundary/Negative | Known-invalid email/password string containing special characters | Enter values; submit | No access is granted; application behavior is **Requires clarification** where no approved requirement defines a limit or format. |
| LGN-011 | Whitespace-only fields – Negative/Required | Email and password each contain only whitespace | Submit the form | Fields must not be accepted as populated valid credentials. Exact trimming and error behavior is **Requires clarification**. |
| LGN-012 | Successful login dashboard verification – Positive | Valid registered test account | After successful login, verify the dashboard/page identity and protected content | The authenticated dashboard opens and protected content is available to the authenticated test role. Dashboard labels and role-specific content are **Requires clarification**. |
| LGN-013 | Repeated invalid login – Negative | Known-invalid credentials | Submit invalid credentials more than once, if permitted in the test environment | Each attempt must not grant access. Account lockout, attempt limits, and cooldown behavior are **Requires clarification** and must not be assumed. |
| LGN-014 | Browser back navigation – Regression/Negative | Complete one successful login, then use the browser back action | Return to the previous view and inspect access | Behavior is **Requires clarification**. Only verify that authentication state is not broken or incorrectly exposed. |

### Environment and Test Data

| Data | Classification | Handling |
|---|---|---|
| Registered valid email and password | Confidential test credentials | Store in an approved secret/test-data mechanism; do not place values in this plan or defect evidence |
| Invalid email | Non-sensitive test data | Use a reserved, non-production address if the environment permits |
| Invalid password | Sensitive test data | Use a clearly known dummy value without exposing it in reports |
| User role/permissions | Configuration data | Obtain an account whose authorized dashboard access is approved for testing |
| Browser and OS details | Test evidence | Record exact browser/OS versions used |

**Test data recommendations:**

- Use separate accounts for valid-login and concurrent test activities where possible.
- Avoid testing against real customer or production identities unless explicitly authorized.
- Mask password values in screenshots, videos, logs, and defect records.
- Reset test state by logging out and closing the test session between scenarios.

## 3. Test Schedule

| Phase | Recommended Activity | Entry Trigger | Deliverable |
|---|---|---|---|
| Test readiness | Confirm environment, accounts, browser support, and open questions | Requirements and environment identified | Readiness confirmation |
| Test design | Review requirements and approve scenario coverage | Readiness confirmation | Updated test plan and scenario list |
| Test execution | Execute positive and negative login scenarios | Entry criteria met | Execution records and evidence |
| Defect handling | Confirm, log, track, and retest defects | A discrepancy is reproduced | Defect reports and retest status |
| Test closure | Evaluate completion and unresolved risk | Exit criteria considered | Test summary and recommendation |

**The detailed date schedule requires project dates and resource availability.**

## 4. Test Deliverables

- This functional test plan.
- Test scenario and execution checklist.
- Test execution evidence and execution status.
- Defect reports for requirement discrepancies.
- Browser/environment coverage record.
- Test summary report.
- Traceability between supplied requirements and executed scenarios.

## 5. Entry and Exit Criteria

### Entry Criteria

| Criterion | Status |
|---|---|
| Login page is available at the approved environment URL. | Required |
| Valid registered test credentials are available securely. | Required |
| Invalid test data is available. | Required |
| Approved browser and environment support are identified. | Required |
| Open questions affecting expected behavior are assigned for clarification. | Recommended before execution |
| Test environment is stable enough to complete login attempts. | Required |

### Exit Criteria

| Criterion | Status |
|---|---|
| All planned login scenarios for approved requirements are executed. | Required |
| Valid-login and invalid-login outcomes match the approved requirements. | Required |
| No open Critical or High severity defect remains unresolved. | Recommended |
| Any unresolved requirement ambiguity is documented, owner-assigned, and assessed for risk. | Required |
| Test evidence and defect status are recorded. | Required |
| Stakeholders accept the test summary or remaining risks. | Recommended |

**Recommendation:** Severity thresholds and defect-acceptance rules should be confirmed with project stakeholders.

## 6. Test Execution

### Execution Controls

- Execute LGN-001 to confirm the primary login flow before negative testing.
- Reuse only approved test accounts and record account alias, not password, in results.
- Capture evidence only after masking confidential values.
- Rerun impacted scenarios after fixes.
- Stop execution and escalate if a valid login unexpectedly exposes protected data to an unauthorized account or if a system-wide authentication outage occurs.

### Defect Severity Guide

| Severity | Example |
|---|---|
| Critical | Invalid credentials gain access to an authenticated dashboard or protected data. |
| High | A valid registered user cannot log in, or mandatory fields do not prevent access. |
| Medium | Login result is inconsistent, incomplete, or exposes a confusing but non-blocking issue. |
| Low | Minor label, alignment, or non-functional presentation defect without authentication impact. |

**Recommendation:** Final severity definitions should follow the project defect-handling policy.

## 7. Test Closure

### Test Closure Checklist

- Record scenario pass, fail, blocked, or not executed status.
- Link defects to scenarios and requirements.
- Reconcile executed scenarios against the final scope.
- Identify residual risk for all blocked or not-run scenarios.
- Obtain QA and stakeholder review of the test summary.

## 8. Tools

| Tool / Type | Purpose | Access Note |
|---|---|---|
| Browser | Execute login scenarios | Use only approved versions |
| Browser developer tools | Inspect redirects, network responses, and UI state | Do not store sensitive response data unnecessarily |
| Screenshot/video recorder | Capture evidence | Mask passwords and personal data |
| Test management tool | Manage scenarios, execution, and evidence | Requires clarification |
| Defect tracker | Record and track defects | Requires clarification |

**Recommendation:** Use the project-approved tools if a standardized toolset is required.

## 9. Risks and Mitigations

| Risk | Impact | Mitigation |
|---|---|---|
| Valid test credentials are unavailable or restricted | Primary positive flow cannot be validated | Securely request an approved registered test account and confirm authorization before execution |
| Public or production login is used without approval | Testing may affect real users or data | Confirm environment and testing authorization; use only approved test accounts |
| Password or session data appears in screenshots or logs | Sensitive information disclosure | Mask credentials and avoid recording secret values |
| Undocumented login behavior is assumed as a requirement | Incorrect pass/fail decisions | Mark missing behavior as Requires clarification and obtain approval |
| Browser support is unknown | Result is not reproducible across supported platforms | Confirm supported browser and OS matrix |
| Third-party authentication or network dependency fails | Login may appear unreliable or unavailable | Record the outage, separate environment failure from product defect, and retest later |
| Invalid account data accidentally matches a real account | Test could create unintended account access or state | Use reserved test identities and do not reuse unknown customer credentials |
| Exact invalid-login message differs due to UI copy update | Requirement traceability may be impacted | Preserve the supplied text for baseline testing and verify any approved copy change separately |

### Open Questions

1. What QA/UAT environment should be used instead of, or for validating, the login page?
2. Are browser and operating-system support matrices approved?
3. What dashboards and role types are valid for authorized test accounts?
4. What exact behavior is expected for missing or whitespace-only email/password values?
5. Are input length, format, and trimming rules approved anywhere else?
6. Is CAPTCHA, social login, MFA, or SSO present and in scope?
7. Is account lockout or a failed-login attempt limit approved?
8. Is any session timeout or remember-me behavior approved for testing?
9. What redirect or direct-URL behavior is approved for unauthenticated dashboard access?
10. Which test management and defect-tracking tools must be used?
11. What are the project’s exact severity, priority, and exit-criteria policies?

## 10. Approvals

| Role | Name | Decision / Comments | Date |
|---|---|---|---|
| QA Lead | | | |
| Product Owner / Requirement Owner | | | |
| Security or Identity Owner | | | |
| Development Owner | | | |

**Document status:** Draft for review and editing.
