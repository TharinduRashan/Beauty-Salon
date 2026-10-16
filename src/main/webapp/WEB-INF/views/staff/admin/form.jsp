<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Staff Member Form" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-12 bg-white flex-grow">
    <div class="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="mb-8">
            <a href="${pageContext.request.contextPath}/admin/staff" class="text-xs text-salon-500 hover:text-salon-800 font-semibold mb-1 block">&larr; Back to Staff</a>
            <h1 class="text-3xl font-serif font-bold text-salon-900">${staff != null ? 'Edit Specialist' : 'Add New Specialist'}</h1>
        </div>

        <form action="${staff != null ? pageContext.request.contextPath.concat('/admin/staff/').concat(staff.id).concat('/edit') : pageContext.request.contextPath.concat('/admin/staff/new')}" method="post" class="space-y-6 bg-salon-50/70 p-8 rounded-3xl border border-salon-100">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div>
                <label for="fullName" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Full Name</label>
                <input type="text" id="fullName" name="fullName" value="<c:out value='${staff.fullName}'/>" required
                       class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900"
                       placeholder="e.g. Samantha Fernando">
            </div>

            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label for="email" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Email Address</label>
                    <input type="email" id="email" name="email" value="<c:out value='${staff.email}'/>" required
                           class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900"
                           placeholder="samantha@glamourbeauty.lk">
                </div>
                <div>
                    <label for="phone" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Phone</label>
                    <input type="tel" id="phone" name="phone" value="<c:out value='${staff.phone}'/>"
                           class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900"
                           placeholder="+94 77 123 4567">
                </div>
            </div>

            <div>
                <label for="specialtiesStr" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Specialties (Comma Separated)</label>
                <input type="text" id="specialtiesStr" name="specialtiesStr" value="<c:out value='${staff.specialtiesAsString}'/>"
                       class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900"
                       placeholder="Hair, Skin, Nails, Body, Beauty">
            </div>

            <div>
                <label for="bio" class="block text-xs font-semibold text-salon-800 uppercase tracking-wider mb-2">Biography &amp; Background</label>
                <textarea id="bio" name="bio" rows="3"
                          class="w-full px-4 py-3 bg-white border border-salon-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-salon-500 text-salon-900"
                          placeholder="Experienced stylist specializing in..."><c:out value="${staff.bio}"/></textarea>
            </div>

            <button type="submit" class="w-full text-sm font-semibold text-white bg-salon-600 hover:bg-salon-700 py-3.5 rounded-2xl shadow-sm transition-all">
                Save Specialist Profile
            </button>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
