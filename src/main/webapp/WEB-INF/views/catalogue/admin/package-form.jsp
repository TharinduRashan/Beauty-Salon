<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Package Form" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8">
            <a href="${pageContext.request.contextPath}/admin/packages" class="text-xs text-salon-500 hover:text-salon-800 font-semibold mb-1 block">&larr; Back to Packages</a>
            <h1 class="text-3xl font-serif font-bold text-salon-900">${pkg != null ? 'Edit Package' : 'Create Package'}</h1>
        </div>

        <form action="${pkg != null ? pageContext.request.contextPath.concat('/admin/packages/').concat(pkg.id).concat('/edit') : pageContext.request.contextPath.concat('/admin/packages/new')}" method="post" class="space-y-6 bg-salon-50/70 p-8 rounded-3xl border border-salon-100">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div>
                <label for="name" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Package Name</label>
                <input type="text" id="name" name="name" value="<c:out value='${pkg.name}'/>" required
                       class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900"
                       placeholder="e.g. Bridal Splendor Ritual">
            </div>

            <div>
                <label class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Included Services</label>
                <div class="grid grid-cols-2 gap-2 bg-white p-4 rounded-2xl border border-salon-200 max-h-48 overflow-y-auto">
                    <c:forEach items="${services}" var="s">
                        <label class="flex items-center space-x-2 text-xs text-salon-800 cursor-pointer">
                            <input type="checkbox" name="serviceIds" value="${s.id}"
                                   ${pkg != null && pkg.serviceIds.contains(s.id) ? 'checked' : ''}
                                   class="rounded text-salon-600 focus:ring-salon-500">
                            <span><c:out value="${s.name}"/> (Rs. ${s.price})</span>
                        </label>
                    </c:forEach>
                </div>
            </div>

            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label for="totalPrice" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Package Price (LKR)</label>
                    <input type="number" id="totalPrice" name="totalPrice" value="${pkg != null ? pkg.totalPrice : 6000}" min="0" step="50" required
                           class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900">
                </div>
                <div>
                    <label for="discountPercent" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Discount %</label>
                    <input type="number" id="discountPercent" name="discountPercent" value="${pkg != null ? pkg.discountPercent : 15}" min="0" max="100" required
                           class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900">
                </div>
            </div>

            <div>
                <label for="description" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Description</label>
                <textarea id="description" name="description" rows="3" required
                          class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900"><c:out value="${pkg.description}"/></textarea>
            </div>

            <button type="submit" class="w-full text-sm font-semibold text-white bg-salon-600 hover:bg-salon-700 py-3.5 rounded-2xl shadow-sm transition-all">
                Save Package
            </button>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
