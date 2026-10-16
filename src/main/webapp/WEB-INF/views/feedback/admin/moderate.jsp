<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Feedback Moderation" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8 pb-4 border-b border-salon-100 flex justify-between items-center">
            <div>
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="text-xs text-salon-500 hover:text-salon-800 font-semibold mb-1 block">&larr; Back to Dashboard</a>
                <h1 class="text-3xl font-serif font-bold text-salon-900">Feedback &amp; Reviews Moderation</h1>
            </div>
        </div>

        <div class="overflow-x-auto bg-white rounded-3xl border border-salon-100 shadow-sm">
            <table class="w-full text-left border-collapse text-sm">
                <thead>
                    <tr class="bg-salon-50/70 border-b border-salon-100 text-xs uppercase tracking-wider text-salon-700">
                        <th class="py-4 px-6 font-semibold">Client</th>
                        <th class="py-4 px-6 font-semibold">Type</th>
                        <th class="py-4 px-6 font-semibold">Rating</th>
                        <th class="py-4 px-6 font-semibold">Review Title &amp; Content</th>
                        <th class="py-4 px-6 font-semibold">Status</th>
                        <th class="py-4 px-6 font-semibold text-right">Moderation</th>
                    </tr>
                </thead>
                <tbody class="divide-y divide-salon-100">
                    <c:forEach items="${feedbacks}" var="f">
                        <tr class="hover:bg-salon-50/40 transition-colors">
                            <td class="py-4 px-6 font-semibold text-salon-900"><c:out value="${f.customerName}"/></td>
                            <td class="py-4 px-6 text-xs text-salon-600">${f.type}</td>
                            <td class="py-4 px-6 text-amber-500 font-bold">${f.rating != null ? f.ratingStars : '-'}</td>
                            <td class="py-4 px-6 max-w-xs">
                                <div class="font-bold text-salon-900 text-xs"><c:out value="${f.title}"/></div>
                                <div class="text-xs text-salon-600 font-light truncate"><c:out value="${f.content}"/></div>
                            </td>
                            <td class="py-4 px-6">
                                <span class="px-2.5 py-1 rounded-full text-xs font-semibold
                                    ${f.status == 'APPROVED' ? 'bg-green-50 text-green-700' : ''}
                                    ${f.status == 'PENDING' ? 'bg-amber-50 text-amber-700' : ''}
                                    ${f.status == 'REJECTED' ? 'bg-red-50 text-red-700' : ''}">
                                    ${f.status}
                                </span>
                            </td>
                            <td class="py-4 px-6 text-right space-x-2">
                                <c:if test="${f.status == 'PENDING' || f.status == 'REJECTED'}">
                                    <form action="${pageContext.request.contextPath}/admin/feedback/${f.id}/approve" method="post" class="inline">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <button type="submit" class="text-xs text-green-600 hover:text-green-800 font-semibold">Approve</button>
                                    </form>
                                </c:if>
                                <c:if test="${f.status == 'PENDING' || f.status == 'APPROVED'}">
                                    <form action="${pageContext.request.contextPath}/admin/feedback/${f.id}/reject" method="post" class="inline">
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                        <button type="submit" class="text-xs text-amber-600 hover:text-amber-800 font-semibold">Reject</button>
                                    </form>
                                </c:if>
                                <form action="${pageContext.request.contextPath}/admin/feedback/${f.id}/delete" method="post" class="inline" onsubmit="return confirm('Delete this review permanently?');">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    <button type="submit" class="text-xs text-red-600 hover:text-red-800 font-semibold">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
