# What is GitHub Copilot app modernization?

GitHub Copilot app modernization is an AI-powered agent that helps modernize applications end to end. It upgrades runtimes and frameworks, migrates applications to Azure, improves security, and guides you through every stage of the process including assessment, planning, code transformations, validation, and deployment. It supports Java, Python, and .NET upgrades with integrated Azure migration workflows.

# Key capabilities

## Assessment
Copilot evaluates your project's readiness for modernization. It analyzes code, configuration, and dependencies, identifies outdated libraries and frameworks, and highlights areas that may require additional remediation before you proceed.

![Solution](https://aka.ms/appmod-java-migration-extension-solution-image)

## Planning
Copilot creates a targeted plan to address specific upgrade or migration needs identified during assessment. It provides recommended steps, updates, and sequencing to guide resolution and support broader workflows.

![Plan](https://aka.ms/appmod-vscode-extension-upgrade-plan-dotnet-image)

## Transformations
Copilot applies automated, intelligent code changes based on your project’s dependencies and modernization goals. It modernizes frameworks and versions with minimal manual effort.

Copilot also accelerates modernization using predefined tasks for [common modernization tasks](https://aka.ms/migrate-github-copilot-app-modernization-for-java-predefined-formula) such as secret management, message queues, and identity services. You can also save and reuse custom tasks for consistency across projects.

![Apply Formula](https://aka.ms/appmod-java-migration-extension-apply-formula-image)

## Security
Copilot ensures modernization is reliable and secure by validating builds and tests throughout the process. It automatically resolves build issues during transformation and runs unit tests to confirm error-free changes, maintaining production pipeline integrity. Security vulnerability management is integrated end-to-end: Copilot scans for CVEs after upgrades, applies security fixes in Agent Mode, and provides a review of all security-related changes to strengthen your security posture and maintain compliance.

![CVE](https://aka.ms/appmod-java-migration-extension-cve-img)

## Containerization and deployment
Copilot completes the modernization workflow by generating Infrastructure as Code assets for Azure deployment, addressing deployment errors automatically, and setting up CI/CD pipelines for continuous integration—taking your application from analysis to production with confidence.


# How to run the Upgrade or Migrate to Azure

You can start modernization in two ways:

- From the Quickstart panel, select one of the options:

    - `Upgrade Runtime & Frameworks` or `Upgrade to a newer version of .NET`
    - `Migrate to Azure`

- Open the Copilot Chat window and type a prompt like: 
    - `Upgrade my solution to a new version of .NET` or `Upgrade my project to a new version of Java`
    - `Migrate to Azure`  

Copilot will analyze your code, prepare the plan, and guide you through the required changes.

# Share feedback
Your feedback is essential as we improve. Share your [thoughts](https://aka.ms/ghcp-appmod/feedback) to help shape the product.

# More information
Learn more and get started today:
- [Overview](https://learn.microsoft.com/azure/developer/github-copilot-app-modernization/overview)
- [Java quickstart guide](https://aka.ms/AM4Jgetstarted)
- [Python quickstart guide](https://aka.ms/ghcp-appmod/python-agent-framework)
- [Find answers to frequently asked questions](https://aka.ms/ghcp-appmod/java-faq)

# License
This extension is licensed under [GitHub Copilot Product Specific Terms](https://github.com/customer-terms/github-copilot-product-specific-terms).

# Trademarks
Authorized use of Microsoft trademarks or logos must follow [Microsoft's Trademark & Brand Guidelines](https://www.microsoft.com/legal/intellectualproperty/trademarks/usage/general).

# Privacy statement
GitHub Copilot app modernization uses GitHub Copilot to make code changes, which does not retain code snippets beyond the immediate session. We do not collect, transmit, or store your custom tasks. Review the [Microsoft Privacy Statement](https://go.microsoft.com/fwlink/?LinkId=521839).

Telemetry metrics are collected and analyzed to track feature usage and effectiveness. Learn more about [telemetry settings in VS Code](https://code.visualstudio.com/docs/configure/telemetry).

# Transparency note
GitHub Copilot app modernization uses AI to make code changes, and AI sometimes makes mistakes. Please review and test all changes before using them in production.

# Disclaimer
Unless otherwise permitted under applicable license(s), users may not decompile, modify, repackage, or redistribute any assets, prompts, or internal tools provided as part of this product without prior written consent from Microsoft.
