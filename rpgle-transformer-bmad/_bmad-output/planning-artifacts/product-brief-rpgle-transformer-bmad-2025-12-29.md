---
stepsCompleted: [1, 2, 3, 4, 5]
inputDocuments:
  - '/Users/lana/Documents/dev/java/smeup/lanmam-method/rpgle-transformer-agents/test-programs/programs/CUST001.rpgle'
  - '/Users/lana/Documents/dev/java/smeup/lanmam-method/rpgle-transformer-agents/test-programs/dds/display-files/CUSTDSP.dds'
  - '/Users/lana/Documents/dev/java/smeup/lanmam-method/rpgle-transformer-agents/test-programs/dds/physical-files/CUSTMAST.dds'
date: 2025-12-29
author: Lana
---

# Product Brief: rpgle-transformer-bmad

<!-- Content will be appended sequentially through collaborative workflow steps -->

## Executive Summary

The rpgle-transformer-bmad project addresses a critical business challenge: migrating 50 legacy RPGLE programs from AS/400 to a modern Spring Boot REST API + React architecture within a 6-month hard deadline, with only a single developer available. Traditional approaches—manual rewrites, commercial modernization tools, or consulting engagements—are either too slow, too expensive, or require team sizes that make the timeline unachievable.

This project leverages 2025's AI capabilities (Claude, LLM-powered agents) combined with the BMAD methodology to create an AI-assisted transformation system that multiplies solo developer productivity to make the impossible possible. Where traditional approaches would require 5-10 developers over 12+ months, this AI-first approach enables one person to systematically transform legacy green-screen applications into modern web applications within the business-mandated timeline.

---

## Core Vision

### Problem Statement

Organizations running critical business applications on AS/400 platforms face an existential dilemma: they must migrate off aging, expensive, inflexible infrastructure, but the scale and complexity of legacy RPGLE codebases make migration prohibitively expensive and slow using traditional methods. When migration deadlines are measured in months rather than years, and when teams consist of solo developers rather than large consulting engagements, existing solutions simply don't work.

### Problem Impact

**For the Business:**
- Continued dependency on expensive AS/400 hardware and licensing
- Inability to find and retain RPGLE developers as talent pool shrinks
- Slow feature development velocity limiting business agility
- Risk of missing compliance or business-critical migration deadlines
- High total cost of ownership for legacy infrastructure

**For the Developer:**
- Impossible task: 50 programs × 6 months ÷ 1 person = mission failure with manual approaches
- Maintaining undocumented legacy code without peer support
- Pressure to deliver on unrealistic timelines
- Career risk tied to legacy platform expertise

### Why Existing Solutions Fall Short

**Commercial Modernization Tools:**
- Expensive licensing (often $100K+)
- Rigid, rule-based transpilation produces poor code quality
- Require significant manual cleanup and refactoring
- Don't understand business logic context
- Still require multiple developers to finish the job

**Manual Rewrite Approach:**
- Requires 5-10 experienced developers for 50-program scope
- 12-18 month timeline minimum for complete rewrite
- High risk of business logic errors during translation
- Expensive consulting engagements ($500K-$2M)
- Not feasible for solo developers or tight deadlines

**Traditional Code Generators:**
- Syntax conversion only, no semantic understanding
- Generate unmaintainable spaghetti code
- Miss business rules embedded in RPG logic
- No modern architecture patterns (REST, React components)
- Require complete rewrite of generated output anyway

### Proposed Solution

The rpgle-transformer-bmad leverages modern AI capabilities—specifically large language models like Claude combined with the BMAD (BMad Method) workflow system—to create an intelligent, AI-assisted transformation pipeline that:

**Core Capabilities:**
- **Semantic Understanding**: AI agents analyze RPGLE business logic, not just syntax
- **Architecture Translation**: Converts green-screen patterns to REST APIs + React UIs
- **Systematic Workflow**: BMAD methodology manages transformation complexity
- **Quality Assurance**: Automated validation of transformation accuracy
- **Solo-Developer Optimized**: Force multiplier enabling one person to achieve team-level output

**Target Architecture:**
- Backend: Spring Boot REST APIs (Java)
- Frontend: React web applications
- Data: Modern relational database migration from AS/400 physical files

### Key Differentiators

**1. AI-First Transformation (Not Rule-Based)**
Unlike commercial tools using rigid transpilation rules, this approach uses LLMs to understand business intent, context, and logic patterns—producing cleaner, more maintainable modern code.

**2. 2025 Timing Advantage**
This solution is only possible NOW. AI capabilities (Claude, code-understanding models) didn't exist 2 years ago. We're leveraging cutting-edge AI to solve a decades-old migration problem.

**3. BMAD Methodology Integration**
Systematic workflow management handles the complexity of 50-program transformation, tracking progress, dependencies, and quality gates that would overwhelm ad-hoc approaches.

**4. Cost & Speed Efficiency**
- Solo developer friendly (no team required)
- Fraction of commercial tool/consulting costs
- 6-month timeline achievable vs. 12-18 months traditional
- Open/flexible vs. vendor lock-in

**5. Real Business Context**
Built from actual production RPGLE programs (like CUST001 customer inquiry), ensuring practical, battle-tested transformation patterns rather than theoretical conversions.

## Target Users

### Primary Users

**The Modern Migration Team**

The primary users of rpgle-transformer-bmad are small development teams (2-3 people) tasked with legacy AS/400 modernization projects. These teams are characterized by:

**Profile: Sarah Chen - Team Lead Persona**
- **Background:** Modern developers with 5-10 years experience in Java/Spring and React
- **RPGLE Knowledge:** Zero. They are modern stack developers who have never worked with AS/400 or RPGLE
- **Typical Situation:** Inherited legacy RPGLE systems with no documentation and no available RPGLE experts
- **Business Pressure:** Hard migration deadlines (typically 6-12 months) with existential consequences for missing them
- **Core Challenge:** Must understand and transform business logic in a language they don't know, without time to become experts

**Key Characteristics:**
- **Team Size:** 2-3 developers, often the only team available for the migration
- **Skill Set:** Strong in modern technologies (Spring Boot, React, REST APIs) but zero legacy platform experience
- **Time Pressure:** Facing impossible timelines that would require 10+ developers using traditional approaches
- **Quality Standards:** Need to produce clean, maintainable code—not quick-and-dirty conversions
- **Risk Profile:** Career and business outcomes tied to successful migration completion

**Success Criteria for Primary Users:**
1. **No RPGLE Learning Required:** AI handles business logic understanding, developers never need to become RPGLE experts
2. **Clean, Maintainable Output:** Generated Spring/React code follows modern patterns and is genuinely maintainable
3. **Transformation Accuracy:** High confidence that the modern code faithfully implements the legacy business logic
4. **Timeline Achievement:** Able to complete 40-50 program migrations within 6-month business deadlines
5. **Team Productivity:** 2-3 person team achieves output that would traditionally require 10+ developers

### Secondary Users

N/A - Current focus is exclusively on the developer teams executing the transformation.

### User Journey

**Phase 1: Discovery**
- Development team receives migration mandate with tight deadline
- Team lead (like Sarah) searches for RPGLE transformation solutions
- Discovers rpgle-transformer-bmad through keywords like "AI RPGLE migration" or "AS/400 modernization tools"
- **First Aha Moment:** Realizes AI can understand the business logic without requiring team to learn RPGLE

**Phase 2: Onboarding & First Transformation**
- Team feeds first RPGLE program + DDS files into the system
- AI analyzes and explains the business logic in plain English
- System generates Spring Boot REST API + React UI components
- Team reviews generated code—sees clean, readable, maintainable modern code
- **Second Aha Moment:** "This looks like code we actually want to maintain!"

**Phase 3: Core Usage - Production Migration**
- Team establishes rhythm: 2-3 programs transformed per week
- Workflow: Feed RPGLE → AI transforms → Developers review/refine → Integrate → Test
- AI handles semantic translation and architecture mapping
- Developers focus on integration, refinement, and validation
- Progress tracking: Steady advancement through the 50-program backlog

**Phase 4: Success & Completion**
- Month 5: All 50 programs transformed to modern stack
- Month 6: Integration testing, refinement, production deployment
- **Ultimate Success Moment:** Migration deadline met with high-quality, maintainable codebase
- Team never had to become RPGLE experts—AI bridged the knowledge gap
- Business continuity preserved, platform modernization achieved

## Success Metrics

### User Success Metrics (Developer Teams)

**Primary Success Criteria:**

1. **Timeline Achievement with Quality**
   - Complete all 50 RPGLE programs transformation within 6-month deadline
   - Output meets high code quality standards (clean, maintainable, follows modern patterns)
   - Success requires BOTH speed AND quality - not one at the expense of the other

2. **Transformation Accuracy**
   - 100% functional equivalence to original RPGLE programs
   - Modern Spring/React code behaves identically to legacy green-screen applications
   - Zero business logic errors or missing features
   - Same outputs for same inputs across all business scenarios

3. **Zero RPGLE Knowledge Required**
   - Developers with only Java/Spring and React skills can successfully use the transformer
   - AI handles all RPGLE semantic understanding and business logic analysis
   - Teams transform programs without learning legacy platform expertise
   - Tool removes the RPGLE learning curve barrier completely

4. **Long-term Code Maintainability**
   - Generated code is clean, well-structured, and follows modern architectural patterns
   - Future developers can easily understand, maintain, and enhance the codebase
   - Code quality prevents technical debt and future rewrites
   - Transformation produces production-grade code, not throwaway prototypes

### Business Objectives

**Ultimate Goal: Complete AS/400 Platform Elimination**

The overarching business objective is to successfully decommission the AS/400 platform entirely, with all production workloads running on the modern Spring Boot + React stack. Success means zero ongoing dependency on legacy infrastructure.

**Critical Business Outcomes:**

1. **Migration Completion**
   - All 50 production RPGLE programs successfully transformed and deployed
   - AS/400 platform fully decommissioned within mandate timeline
   - Business continuity maintained throughout migration process

2. **Modern Technology Stack**
   - Production systems running on Spring Boot REST APIs (backend)
   - Modern React web applications (frontend)
   - Elimination of green-screen interfaces and legacy dependencies

3. **Business Risk Mitigation**
   - Avoid business unit shutdown or compliance failures due to missed deadlines
   - Preserve critical business processes during platform transition
   - Enable business agility through modern, flexible architecture

### Key Performance Indicators (KPIs)

**Transformation Velocity:**
- **Target:** 2-3 programs successfully transformed per week
- **Measurement:** Programs moved from RPGLE to production-ready Spring/React per week
- **Goal:** Maintain consistent velocity to achieve 50-program completion within 6 months

**Functional Accuracy Rate:**
- **Target:** 100% functional equivalence
- **Measurement:** Percentage of business logic correctly preserved in transformation
- **Validation:** Automated testing, regression testing, user acceptance testing
- **Threshold:** Zero tolerance for business logic errors in production

**Code Quality Metrics:**
- **Maintainability Index:** Generated code meets or exceeds industry standards for Spring/React applications
- **Technical Debt:** Zero high-severity code smells or anti-patterns in generated output
- **Documentation:** All generated components include clear documentation and comments
- **Test Coverage:** Adequate test coverage for transformed business logic

**Developer Productivity:**
- **RPGLE Learning Time:** Zero hours spent learning legacy platform
- **Review/Integration Time:** Developers spend time on review and integration, not translation
- **Team Size Efficiency:** 2-3 person team achieves output equivalent to 10+ traditional developers

**Platform Migration Success:**
- **AS/400 Decommission:** Complete elimination of legacy platform dependency
- **Production Deployment:** All 50 programs running successfully in production on modern stack
- **Business Continuity:** Zero business process disruptions during migration
- **Timeline Adherence:** Migration completed within 6-month business mandate

## MVP Scope

### Core Features

The MVP focuses on proving the AI-assisted transformation approach with a single, complete end-to-end example before scaling to the full 50-program migration.

**Primary Deliverable: CUST001 Customer Inquiry Transformation**

Transform the CUST001 RPGLE program (including CUSTDSP.dds display file and CUSTMAST.dds physical file) into a fully functional modern web application running in the browser.

**Essential MVP Capabilities:**

1. **AI Transformation Pipeline**
   - Input: RPGLE source code + DDS display files + DDS physical files
   - AI semantic understanding of RPGLE business logic (not just syntax conversion)
   - Output: Spring Boot REST API + React web application
   - Preservation of 100% functional equivalence

2. **Spring Boot Backend Generation**
   - RESTful API endpoints for customer inquiry operations
   - JPA entities mapping DDS physical file structure to modern database
   - Service layer implementing RPGLE business logic
   - Clean, maintainable Java code following Spring Boot best practices

3. **React Frontend Generation**
   - Customer inquiry UI (number entry + detail display)
   - Functional equivalent to green-screen PROMPT and DETAIL formats
   - Modern, responsive web interface
   - Component-based architecture following React best practices

4. **Automated Test Generation** *(Fundamental Requirement)*
   - Automated test suite generated alongside application code
   - Tests validate 100% functional equivalence to original RPGLE behavior
   - Coverage of all business logic scenarios and edge cases
   - Provides confidence in transformation accuracy without manual verification

5. **Developer Experience**
   - Transformation workflow usable by developers with zero RPGLE knowledge
   - AI explains business logic in plain English
   - Generated code is readable and maintainable by modern Java/React developers
   - Process is repeatable and ready to scale to remaining programs

### Out of Scope for MVP

The following capabilities are explicitly excluded from MVP to maintain focus on proving the core transformation approach:

**Deferred to Post-MVP:**

1. **Remaining 49 Programs**
   - MVP validates approach with CUST001 only
   - Full 50-program migration happens after MVP success proves the concept

2. **Advanced UI Features**
   - Dashboards, analytics, or reporting beyond basic CRUD
   - Enhanced UX features beyond functional equivalence
   - Progressive web app capabilities

3. **Authentication & Authorization**
   - User login and access control
   - Role-based permissions
   - Security features beyond basic application security

4. **Production Deployment Infrastructure**
   - CI/CD pipelines
   - Container orchestration (Kubernetes, etc.)
   - Production monitoring and observability
   - Deployment automation

5. **Performance Optimization**
   - Advanced query optimization
   - Caching strategies
   - Load balancing
   - Scalability tuning

6. **Multi-User Collaboration Features**
   - Team workflows for parallel development
   - Transformation progress tracking across teams
   - Code review and approval workflows
   - *(Interesting for future when scaling to 50 programs with larger teams)*

### MVP Success Criteria

The MVP is considered successful when the following conditions are met:

**Functional Success:**
- CUST001 React application runs successfully in web browser
- Customer lookup functionality works (user enters customer number, sees customer details)
- Application behavior is functionally equivalent to original RPGLE green-screen program
- All RPGLE business logic correctly preserved in modern implementation

**Test Validation:**
- Automated test suite generated successfully
- All generated tests pass, validating functional equivalence
- Test coverage includes all business logic scenarios from original RPGLE program
- Tests provide confidence for scaling approach to remaining programs

**Code Quality:**
- Generated Spring Boot code is clean, well-structured, and maintainable
- Generated React code follows modern component patterns and best practices
- Code is understandable and modifiable by developers unfamiliar with RPGLE
- No critical code smells or anti-patterns in generated output

**Developer Experience:**
- Transformation completed by developer with zero RPGLE knowledge
- AI successfully explained RPGLE business logic without requiring legacy platform learning
- Transformation process is documented and repeatable
- Developer confidence in scaling approach to next 49 programs

**Go/No-Go Decision:**
If MVP succeeds, proceed to transform all 50 programs using the proven AI-assisted approach within the 6-month timeline.

### Future Vision

**Phase 2: Full Migration (Post-MVP)**

Once CUST001 validates the transformation approach, scale to complete the 50-program AS/400 migration:
- Apply proven transformation pipeline to remaining 49 RPGLE programs
- Maintain velocity of 2-3 programs per week
- Achieve 6-month migration timeline and AS/400 decommissioning goal

**Enhanced Capabilities:**

1. **Team Collaboration Features**
   - Multi-developer workflows for parallel transformation work
   - Progress tracking and coordination across the 50-program backlog
   - Code review and validation workflows
   - Integration with team development practices

2. **Advanced Testing & Quality**
   - More sophisticated test scenario generation
   - Edge case and boundary condition coverage
   - Integration testing across transformed programs
   - Regression testing automation

3. **Performance & Optimization**
   - Generated code optimization for production workloads
   - Database query performance tuning
   - Frontend performance optimization
   - Faster transformation processing for high-volume scenarios

4. **Production Readiness**
   - Authentication and authorization implementation
   - Production deployment infrastructure and automation
   - Monitoring, logging, and observability
   - Security hardening and compliance validation

**Long-term Platform Vision (2-3 Years):**

1. **Expansion Beyond Initial 50 Programs**
   - Apply transformation approach to additional RPGLE systems
   - Build library of reusable transformation patterns
   - Handle increasingly complex RPGLE program types

2. **Platform for RPGLE Modernization**
   - Generalize approach beyond single organization
   - Reusable patterns for common RPGLE use cases (CRUD, batch processing, reports)
   - Knowledge base of AS/400 → modern stack transformations
   - Potential offering for other organizations facing similar legacy migrations

3. **Broader Legacy Modernization**
   - Expand AI transformation capabilities to other legacy platforms (COBOL, PL/I, etc.)
   - Multi-platform modernization toolkit
   - Comprehensive legacy-to-modern transformation platform
