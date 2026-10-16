<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Client Reviews" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex flex-col md:flex-row justify-between items-end mb-12 border-b border-salon-100 pb-8">
            <div>
                <span class="text-xs uppercase tracking-widest font-semibold text-salon-500">Unfiltered Feedback</span>
                <h1 class="text-4xl font-serif font-bold text-salon-900 mt-2">Client Testimonials</h1>
                <p class="text-sm text-salon-600 mt-2">Hear directly from the guests who entrust us with their personal aesthetic journeys.</p>
            </div>
            <a href="${pageContext.request.contextPath}/customer/feedback/submit"
               class="mt-4 md:mt-0 text-xs font-bold uppercase tracking-wider text-white bg-salon-600 hover:bg-salon-700 px-6 py-3 rounded-full transition-all shadow-sm">
                Write a Review
            </a>
        </div>

        <c:if test="${empty reviews}">
            <div class="text-center py-20 bg-salon-50 rounded-3xl border border-dashed border-salon-200">
                <i class="fa-regular fa-comments text-salon-300 text-4xl mb-3"></i>
                <h3 class="font-serif text-lg font-bold text-salon-800">No Reviews Published Yet</h3>
                <p class="text-xs text-salon-500 mt-1">Be the first to share your journey with our community!</p>
            </div>
        </c:if>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            <c:forEach items="${reviews}" var="r">
                <div class="bg-salon-50/50 rounded-3xl p-8 border border-salon-100 shadow-sm flex flex-col justify-between">
                    <div>
                        <div class="text-amber-400 text-sm mb-3 tracking-widest">
                            ${r.ratingStars}
                        </div>
                        <h3 class="font-serif text-lg font-bold text-salon-900 mb-2"><c:out value="${r.title}"/></h3>
                        <p class="text-xs sm:text-sm text-salon-600 leading-relaxed font-light mb-6">"<c:out value="${r.content}"/>"</p>
                    </div>
                    <div class="pt-4 border-t border-salon-100 flex items-center justify-between text-xs text-salon-500">
                        <span class="font-semibold text-salon-900"><c:out value="${r.customerName != null ? r.customerName : 'Verified Customer'}"/></span>
                        <span><c:out value="${r.formattedCreatedAt}"/></span>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
