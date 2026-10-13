<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Appointment Details" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8 flex justify-between items-center">
            <div>
                <a href="${pageContext.request.contextPath}/customer/appointments" class="text-xs text-salon-500 hover:text-salon-800 font-semibold mb-1 block">
                    &larr; Back to Appointments
                </a>
                <h1 class="text-3xl font-serif font-bold text-salon-900">Appointment Confirmation</h1>
            </div>
            <span class="inline-flex items-center px-3.5 py-1.5 rounded-full text-xs font-semibold
                ${appointment.status == 'CONFIRMED' ? 'bg-blue-50 text-blue-700 border border-blue-200' : ''}
                ${appointment.status == 'COMPLETED' ? 'bg-green-50 text-green-700 border border-green-200' : ''}
                ${appointment.status == 'CANCELLED' ? 'bg-red-50 text-red-700 border border-red-200' : ''}
                ${appointment.status == 'PENDING' ? 'bg-amber-50 text-amber-700 border border-amber-200' : ''}">
                ${appointment.status}
            </span>
        </div>

        <div class="bg-salon-50/70 rounded-3xl p-8 sm:p-10 border border-salon-100 shadow-sm space-y-8">
            <!-- Reservation Summary Box -->
            <div class="bg-white p-6 sm:p-8 rounded-2xl border border-salon-100 shadow-sm space-y-6">
                <div class="flex items-center space-x-4 pb-6 border-b border-salon-100">
                    <div class="w-14 h-14 rounded-2xl bg-salon-50 text-salon-600 flex items-center justify-center text-2xl shadow-sm">
                        <i class="fa-solid fa-spa"></i>
                    </div>
                    <div>
                        <c:choose>
                            <c:when test="${not empty appointment.packageId}">
                                <span class="px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider bg-purple-50 text-purple-700 border border-purple-200">
                                    Curated Spa Experience
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="text-xs uppercase tracking-wider text-salon-400 font-semibold">Reserved Service</span>
                            </c:otherwise>
                        </c:choose>
                        <h2 class="text-2xl font-serif font-bold text-salon-900 mt-0.5"><c:out value="${appointment.serviceNameSnapshot}"/></h2>
                    </div>
                </div>

                <div class="grid grid-cols-1 sm:grid-cols-2 gap-6 text-sm">
                    <div>
                        <span class="text-xs text-salon-400 block font-light">Scheduled Date</span>
                        <div class="text-base font-bold text-salon-900 mt-0.5">
                            <i class="fa-regular fa-calendar mr-1.5 text-salon-500"></i> ${appointment.formattedDate}
                        </div>
                    </div>
                    <div>
                        <span class="text-xs text-salon-400 block font-light">Time Slot Window</span>
                        <div class="text-base font-bold text-salon-900 mt-0.5">
                            <i class="fa-regular fa-clock mr-1.5 text-salon-500"></i> ${appointment.formattedStartTime} - ${appointment.formattedEndTime} (${appointment.durationMinutes} min)
                        </div>
                    </div>
                    <div>
                        <span class="text-xs text-salon-400 block font-light">Stylist / Specialist</span>
                        <div class="text-base font-bold text-salon-900 mt-0.5">
                            <i class="fa-regular fa-user mr-1.5 text-salon-500"></i> <c:out value="${staff.fullName != null ? staff.fullName : 'Assigned Specialist'}"/>
                        </div>
                    </div>
                    <div>
                        <span class="text-xs text-salon-400 block font-light">Estimated Fee</span>
                        <div class="text-xl font-serif font-bold text-salon-900 mt-0.5">
                            Rs. ${appointment.priceSnapshot}
                        </div>
                    </div>
                </div>

                <c:if test="${not empty appointment.notes}">
                    <div class="pt-4 border-t border-salon-100">
                        <span class="text-xs text-salon-400 block font-light mb-1">Client Requests / Notes:</span>
                        <p class="text-xs text-salon-700 bg-salon-50 p-3 rounded-xl border border-salon-100 italic">
                            "<c:out value="${appointment.notes}"/>"
                        </p>
                    </div>
                </c:if>
            </div>

            <!-- Action Buttons -->
            <div class="flex flex-col sm:flex-row items-center justify-between gap-4 pt-2">
                <a href="${pageContext.request.contextPath}/customer/appointments" class="text-xs font-semibold text-salon-600 hover:text-salon-800">
                    &larr; View All My Appointments
                </a>

                <div class="flex items-center space-x-3 w-full sm:w-auto">
                    <c:if test="${appointment.status == 'CONFIRMED'}">
                        <a href="${pageContext.request.contextPath}/customer/payments/checkout/${appointment.id}"
                           class="w-full sm:w-auto text-center text-xs font-bold uppercase tracking-wider text-white bg-salon-600 hover:bg-salon-700 px-6 py-3 rounded-full shadow-md transition-all">
                            Proceed to Payment &rarr;
                        </a>
                    </c:if>

                    <c:if test="${appointment.cancellable}">
                        <form action="${pageContext.request.contextPath}/customer/appointments/${appointment.id}/cancel" method="post" class="inline m-0" onsubmit="return confirm('Are you sure you wish to cancel this appointment?');">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="text-xs font-semibold text-red-600 hover:text-red-800 px-4 py-3 border border-red-200 rounded-full hover:bg-red-50 transition-all">
                                Cancel Booking
                            </button>
                        </form>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
