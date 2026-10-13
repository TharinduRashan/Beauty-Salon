<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Curated Packages" scope="request"/>
<jsp:include page="/WEB-INF/views/common/layout-head.jsp" />

<div class="py-16 bg-white flex-grow">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        <!-- Header Banner -->
        <div class="text-center max-w-2xl mx-auto mb-16">
            <span class="text-xs uppercase tracking-widest font-semibold text-salon-500 bg-salon-50 px-4 py-1.5 rounded-full border border-salon-100">
                Curated Luxury Experiences
            </span>
            <h1 class="text-4xl sm:text-5xl font-serif font-bold text-salon-900 mt-4">Bespoke Rituals &amp; Packages</h1>
            <p class="text-sm text-salon-600 mt-3 font-light leading-relaxed">
                Harmoniously bundled therapies designed for comprehensive head-to-toe pampering with preferential package pricing.
            </p>
        </div>

        <c:if test="${empty packages}">
            <div class="text-center py-20 bg-salon-50 rounded-3xl border border-dashed border-salon-200">
                <i class="fa-solid fa-gift text-salon-300 text-5xl mb-4"></i>
                <h3 class="font-serif text-xl font-bold text-salon-800">New Bundles Coming Soon</h3>
                <p class="text-xs sm:text-sm text-salon-500 mt-1 max-w-md mx-auto">
                    Our seasonal wellness packages are being curated. In the meantime, browse our individual treatment offerings!
                </p>
                <a href="${pageContext.request.contextPath}/services" class="inline-block mt-5 text-xs font-bold uppercase tracking-wider text-white bg-salon-600 hover:bg-salon-700 px-6 py-3 rounded-full transition-all shadow-sm">
                    Browse Services
                </a>
            </div>
        </c:if>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            <c:forEach items="${packages}" var="p">
                <!-- Package Curated Photography Selector -->
                <c:set var="pkgImg" value="https://images.unsplash.com/photo-1540555700478-4be289fbecef?auto=format&fit=crop&w=800&q=80"/>
                <c:set var="pkgBadge" value="Spa Ritual"/>
                
                <c:if test="${p.name.contains('Bridal')}">
                    <c:set var="pkgImg" value="https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&w=800&q=80"/>
                    <c:set var="pkgBadge" value="Bridal Glow"/>
                </c:if>
                <c:if test="${p.name.contains('Hair & Nails') or p.name.contains('Indulgence')}">
                    <c:set var="pkgImg" value="https://images.unsplash.com/photo-1560066984-138dadb4c035?auto=format&fit=crop&w=800&q=80"/>
                    <c:set var="pkgBadge" value="Hair & Nails"/>
                </c:if>
                <c:if test="${p.name.contains('Rejuvenation') or p.name.contains('Retreat')}">
                    <c:set var="pkgImg" value="https://images.unsplash.com/photo-1544161515-4ab6ce6db874?auto=format&fit=crop&w=800&q=80"/>
                    <c:set var="pkgBadge" value="Full Body Revival"/>
                </c:if>

                <div class="bg-white rounded-3xl overflow-hidden border border-salon-100 shadow-sm hover:shadow-xl hover:border-salon-300 transition-all duration-300 flex flex-col justify-between group">
                    <div>
                        <!-- Eye-Catching Package Imagery -->
                        <div class="relative h-56 sm:h-60 overflow-hidden bg-salon-100">
                            <img src="${pkgImg}" alt="<c:out value='${p.name}'/>" class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-700">
                            
                            <!-- Category Badge -->
                            <span class="absolute top-4 left-4 px-3.5 py-1 rounded-full text-[11px] font-semibold uppercase tracking-wider bg-white/95 backdrop-blur-md text-salon-900 shadow-sm border border-white/50">
                                <i class="fa-solid fa-sparkles text-salon-500 mr-1"></i> ${pkgBadge}
                            </span>
                            
                            <!-- Savings Badge -->
                            <span class="absolute top-4 right-4 px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider bg-salon-700 text-white shadow-md">
                                Save ${p.discountPercent}%
                            </span>

                            <!-- Floating Bottom Gradient -->
                            <div class="absolute inset-x-0 bottom-0 h-16 bg-gradient-to-t from-black/60 to-transparent flex items-end p-4">
                                <span class="text-white text-xs font-medium flex items-center">
                                    <i class="fa-solid fa-layer-group text-salon-300 mr-1.5"></i> Multi-Service Experience
                                </span>
                            </div>
                        </div>

                        <!-- Card Body -->
                        <div class="p-6 sm:p-7">
                            <h3 class="font-serif text-2xl font-bold text-salon-900 mb-2.5 group-hover:text-salon-700 transition-colors">
                                <c:out value="${p.name}"/>
                            </h3>
                            <p class="text-xs sm:text-sm text-salon-600 leading-relaxed font-light mb-6">
                                <c:out value="${p.description}"/>
                            </p>
                        </div>
                    </div>

                    <!-- Footer Pricing & Action -->
                    <div class="p-6 sm:p-7 pt-0 mt-auto">
                        <div class="pt-5 border-t border-salon-100 flex items-center justify-between">
                            <div>
                                <span class="text-[10px] uppercase tracking-wider text-salon-400 block font-light">Complete Package</span>
                                <span class="text-2xl font-serif font-bold text-salon-900">Rs. ${p.totalPrice}</span>
                            </div>
                            <a href="${pageContext.request.contextPath}/customer/appointments/book?packageId=${p.id}"
                               class="text-xs font-bold uppercase tracking-wider text-white bg-salon-800 hover:bg-black px-6 py-3 rounded-full transition-all shadow-sm hover:shadow">
                                Book Experience &rarr;
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp" />
