<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Submit Feedback" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8">
            <h1 class="text-3xl font-serif font-bold text-salon-900">Share Your Experience</h1>
            <p class="text-xs text-salon-500 mt-1">We value honest impressions to elevate our sanctuary's standards.</p>
        </div>

        <form action="${pageContext.request.contextPath}/customer/feedback/submit" method="post" class="space-y-6 bg-salon-50/70 p-8 rounded-3xl border border-salon-100">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div>
                <label for="type" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Category</label>
                <select id="type" name="type" required class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900">
                    <option value="REVIEW" selected>Treatment Review</option>
                    <option value="GENERAL">General Feedback</option>
                    <option value="COMPLAINT">Service Concern / Complaint</option>
                </select>
            </div>

            <c:if test="${not empty completedAppointments}">
                <div>
                    <label for="appointmentId" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Linked Completed Appointment</label>
                    <select id="appointmentId" name="appointmentId" class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900">
                        <option value="">-- Choose Completed Treatment --</option>
                        <c:forEach items="${completedAppointments}" var="ca">
                            <option value="${ca.id}" ${param.appointmentId == ca.id ? 'selected' : ''}>
                                ${ca.serviceNameSnapshot} on ${ca.formattedDate}
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </c:if>

            <div>
                <label for="rating" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Rating</label>
                <select id="rating" name="rating" class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900">
                    <option value="5" selected>★★★★★ - Exceptional</option>
                    <option value="4">★★★★☆ - Very Good</option>
                    <option value="3">★★★☆☆ - Average</option>
                    <option value="2">★★☆☆☆ - Needs Improvement</option>
                    <option value="1">★☆☆☆☆ - Unsatisfactory</option>
                </select>
            </div>

            <div>
                <label for="title" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Headline / Title</label>
                <input type="text" id="title" name="title" required
                       class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900"
                       placeholder="e.g. Sublime styling experience">
            </div>

            <div>
                <label for="content" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Detailed Comments</label>
                <textarea id="content" name="content" rows="4" required minlength="10" maxlength="1000"
                          class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900"
                          placeholder="Tell us what you loved or where we can improve..."></textarea>
            </div>

            <button type="submit" class="w-full text-sm font-semibold text-white bg-salon-600 hover:bg-salon-700 py-3.5 rounded-2xl shadow-sm transition-all">
                Submit Testimonial
            </button>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
