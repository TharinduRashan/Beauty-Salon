<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My Feedback" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8 pb-4 border-b border-salon-100 flex justify-between items-center">
            <div>
                <span class="text-xs uppercase tracking-widest font-semibold text-salon-500">Testimonials</span>
                <h1 class="text-3xl font-serif font-bold text-salon-900 mt-1">My Submitted Reviews</h1>
            </div>
            <a href="${pageContext.request.contextPath}/customer/feedback/submit"
               class="text-xs font-bold uppercase tracking-wider text-white bg-salon-600 hover:bg-salon-700 px-6 py-3 rounded-full transition-all">
                + Submit Feedback
            </a>
        </div>

        <c:choose>
            <c:when test="${empty feedbacks}">
                <div class="text-center py-20 bg-salon-50 rounded-3xl border border-dashed border-salon-200">
                    <i class="fa-regular fa-star text-salon-300 text-4xl mb-3"></i>
                    <h3 class="font-serif text-lg font-bold text-salon-800">No Feedback Submitted</h3>
                    <p class="text-xs text-salon-500 mt-1">Share your experience to help us continually refine our craft.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <c:forEach items="${feedbacks}" var="f">
                        <div class="p-6 rounded-3xl bg-salon-50/70 border border-salon-100 flex flex-col justify-between">
                            <div>
                                <div class="flex justify-between items-center mb-2">
                                    <span class="text-amber-400 text-xs">${f.ratingStars}</span>
                                    <span class="px-2 py-0.5 rounded-full text-xs font-semibold
                                        ${f.status == 'APPROVED' ? 'bg-green-50 text-green-700' : 'bg-amber-50 text-amber-700'}">
                                        ${f.status}
                                    </span>
                                </div>
                                <h4 class="font-serif font-bold text-salon-900 mb-1"><c:out value="${f.title}"/></h4>
                                <p class="text-xs text-salon-600 leading-relaxed font-light mb-4">"<c:out value="${f.content}"/>"</p>
                            </div>
                            <span class="text-xs text-salon-400 block">${f.formattedCreatedAt}</span>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
