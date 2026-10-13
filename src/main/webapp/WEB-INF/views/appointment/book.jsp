<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Book Appointment &amp; Packages" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<c:set var="isPackageMode" value="${not empty param.packageId or not empty selectedPackage}"/>

<div class="py-12 bg-white flex-grow">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-8">
            <span class="text-xs uppercase tracking-widest font-semibold text-salon-500">Effortless Reservation</span>
            <h1 class="text-3xl font-serif font-bold text-salon-900 mt-2">Reserve Your Experience</h1>
            <p class="text-xs sm:text-sm text-salon-600 mt-2">Select an individual treatment or an all-inclusive curated spa package.</p>
            
            <!-- Mode Switcher Tabs -->
            <div class="inline-flex mt-6 p-1.5 bg-salon-100 rounded-full border border-salon-200">
                <a href="${pageContext.request.contextPath}/customer/appointments/book"
                   class="px-5 py-2 rounded-full text-xs font-semibold transition-all ${not isPackageMode ? 'bg-white text-salon-900 shadow-sm' : 'text-salon-600 hover:text-salon-900'}">
                    <i class="fa-solid fa-scissors mr-1.5"></i> Single Service
                </a>
                <a href="${pageContext.request.contextPath}/customer/appointments/book?packageId=${not empty packages ? packages[0].id : ''}"
                   class="px-5 py-2 rounded-full text-xs font-semibold transition-all ${isPackageMode ? 'bg-white text-salon-900 shadow-sm' : 'text-salon-600 hover:text-salon-900'}">
                    <i class="fa-solid fa-box-open mr-1.5"></i> Curated Package
                </a>
            </div>
        </div>

        <div class="bg-salon-50/70 rounded-3xl p-8 sm:p-10 border border-salon-100 shadow-sm">
            <form id="slotForm" action="${pageContext.request.contextPath}/customer/appointments/book" method="get" class="space-y-6">
                
                <c:if test="${isPackageMode}">
                    <!-- Package Selection Mode -->
                    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                        <div class="md:col-span-1">
                            <label for="packageSelect" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">
                                1. Curated Package
                            </label>
                            <select id="packageSelect" name="packageId" onchange="this.form.submit()" required
                                    class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900 shadow-sm">
                                <option value="">-- Choose Package --</option>
                                <c:forEach items="${packages}" var="p">
                                    <option value="${p.id}" ${param.packageId == p.id ? 'selected' : ''}>
                                        ${p.name} (Rs. ${p.totalPrice})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Specialist Select -->
                        <div>
                            <label for="staffSelect" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">
                                2. Specialist / Stylist
                            </label>
                            <select id="staffSelect" name="staffId" onchange="this.form.submit()" required
                                    class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900 shadow-sm">
                                <option value="">-- Choose Specialist --</option>
                                <c:forEach items="${staff}" var="st">
                                    <option value="${st.id}" ${param.staffId == st.id ? 'selected' : ''}>
                                        ${st.fullName} (${st.specialtiesAsString})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Date Select -->
                        <div>
                            <label for="dateSelect" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">
                                3. Appointment Date
                            </label>
                            <input type="date" id="dateSelect" name="date" value="${param.date}" onchange="this.form.submit()" required
                                   class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900 shadow-sm">
                        </div>
                    </div>

                    <!-- Package Highlight Banner -->
                    <c:if test="${not empty selectedPackage}">
                        <div class="mt-4 p-5 rounded-2xl bg-white border border-salon-200/80 shadow-sm flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
                            <div>
                                <div class="flex items-center space-x-2">
                                    <span class="px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider bg-purple-50 text-purple-700 border border-purple-200">
                                        Package Experience
                                    </span>
                                    <span class="px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider bg-green-50 text-green-700 border border-green-200">
                                        ${selectedPackage.discountPercent}% OFF Included
                                    </span>
                                </div>
                                <h3 class="text-lg font-serif font-bold text-salon-900 mt-1">${selectedPackage.name}</h3>
                                <p class="text-xs text-salon-600 mt-0.5">${selectedPackage.description}</p>
                            </div>
                            <div class="text-right sm:border-l sm:border-salon-100 sm:pl-6">
                                <span class="text-xs text-salon-400 block font-light">Bundle Price</span>
                                <span class="text-xl font-serif font-bold text-salon-900">Rs. ${selectedPackage.totalPrice}</span>
                                <span class="text-[11px] text-salon-500 block">Est. Duration: ${packageDuration} min</span>
                            </div>
                        </div>
                    </c:if>
                </c:if>

                <c:if test="${not isPackageMode}">
                    <!-- Single Service Selection Mode -->
                    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                        <div>
                            <label for="serviceSelect" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">
                                1. Select Service
                            </label>
                            <select id="serviceSelect" name="serviceId" onchange="this.form.submit()" required
                                    class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900 shadow-sm">
                                <option value="">-- Choose a Service --</option>
                                <c:forEach items="${services}" var="s">
                                    <option value="${s.id}" ${param.serviceId == s.id ? 'selected' : ''}>
                                        ${s.name} (Rs. ${s.price} - ${s.durationFormatted})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Staff Select -->
                        <div>
                            <label for="staffSelect" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">
                                2. Specialist / Stylist
                            </label>
                            <select id="staffSelect" name="staffId" onchange="this.form.submit()" required
                                    class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900 shadow-sm">
                                <option value="">-- Choose Specialist --</option>
                                <c:forEach items="${staff}" var="st">
                                    <option value="${st.id}" ${param.staffId == st.id ? 'selected' : ''}>
                                        ${st.fullName} (${st.specialtiesAsString})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Date Select -->
                        <div>
                            <label for="dateSelect" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">
                                3. Appointment Date
                            </label>
                            <input type="date" id="dateSelect" name="date" value="${param.date}" onchange="this.form.submit()" required
                                   class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900 shadow-sm">
                        </div>
                    </div>
                </c:if>

            </form>

            <!-- Time Slots & Reservation Submission Section -->
            <c:if test="${(not empty param.serviceId or not empty param.packageId) and not empty param.staffId and not empty param.date}">
                <hr class="my-8 border-salon-200/60">

                <form action="${pageContext.request.contextPath}/customer/appointments/book" method="post" class="space-y-6">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <c:if test="${not empty param.packageId}">
                        <input type="hidden" name="packageId" value="${param.packageId}"/>
                    </c:if>
                    <c:if test="${not empty param.serviceId}">
                        <input type="hidden" name="serviceId" value="${param.serviceId}"/>
                    </c:if>
                    <input type="hidden" name="staffId" value="${param.staffId}"/>
                    <input type="hidden" name="date" value="${param.date}"/>

                    <div>
                        <div class="flex items-center justify-between mb-3">
                            <label class="block text-xs font-semibold text-salon-800 uppercase tracking-wider">
                                4. Select Start Time Slot
                            </label>
                            <span class="text-xs text-salon-500">
                                <c:choose>
                                    <c:when test="${not empty selectedPackage}">
                                        Duration: <strong>${packageDuration} minutes</strong>
                                    </c:when>
                                    <c:when test="${not empty selectedService}">
                                        Duration: <strong>${selectedService.durationFormatted}</strong>
                                    </c:when>
                                </c:choose>
                            </span>
                        </div>

                        <c:choose>
                            <c:when test="${empty availableSlots}">
                                <div class="p-4 bg-amber-50 rounded-2xl border border-amber-200 text-xs text-amber-800">
                                    <i class="fa-solid fa-circle-info mr-2"></i> No sufficient consecutive time slots are available for this specialist on this date. Please select another date or specialist.
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="grid grid-cols-3 sm:grid-cols-4 md:grid-cols-6 gap-3">
                                    <c:forEach items="${availableSlots}" var="slot">
                                        <label class="cursor-pointer">
                                            <input type="radio" name="startTime" value="${slot}" required class="peer sr-only">
                                            <div class="px-3 py-2.5 text-center text-xs font-semibold rounded-xl bg-white border border-salon-200 text-salon-800 peer-checked:bg-salon-600 peer-checked:text-white peer-checked:border-salon-600 hover:bg-salon-50 transition-all shadow-sm">
                                                ${slot}
                                            </div>
                                        </label>
                                    </c:forEach>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div>
                        <label for="notes" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Special Requests / Preferences</label>
                        <textarea id="notes" name="notes" rows="3"
                                  class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900"
                                  placeholder="Let us know if you have any sensitivities or specific requests for your specialist..."></textarea>
                    </div>

                    <c:if test="${not empty availableSlots}">
                        <div class="pt-4 flex flex-col sm:flex-row items-center justify-between gap-4">
                            <div class="text-xs text-salon-500">
                                <i class="fa-solid fa-shield-halved text-salon-400 mr-1"></i> You can cancel or reschedule up to 24 hours prior to appointment.
                            </div>
                            <button type="submit"
                                    class="w-full sm:w-auto text-xs font-bold uppercase tracking-wider text-white bg-salon-600 hover:bg-salon-700 px-8 py-4 rounded-full shadow-lg shadow-salon-500/25 transition-all">
                                Confirm &amp; Reserve ${not empty param.packageId ? 'Package Experience' : 'Treatment'}
                            </button>
                        </div>
                    </c:if>
                </form>
            </c:if>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
