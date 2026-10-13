<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<footer class="mt-auto bg-white border-t border-salon-100 text-salon-800 pt-16 pb-12">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-10">
            <!-- Brand -->
            <div class="col-span-1 md:col-span-2">
                <div class="flex items-center space-x-2 mb-4">
                    <span class="w-8 h-8 rounded-full bg-salon-600 flex items-center justify-center text-white">
                        <i class="fa-solid fa-spa text-sm"></i>
                    </span>
                    <span class="text-xl font-serif font-bold text-salon-900">Glamour <span class="text-salon-500 font-light italic">Salon</span></span>
                </div>
                <p class="text-sm text-salon-700 max-w-sm mb-6 leading-relaxed">
                    A boutique sanctuary dedicated to elevated wellness, curated aesthetics, and individualized beauty experiences. Discover true tranquility.
                </p>
                <div class="flex space-x-4 text-salon-500">
                    <a href="#" class="w-8 h-8 rounded-full bg-salon-50 flex items-center justify-center hover:bg-salon-600 hover:text-white transition-all"><i class="fa-brands fa-instagram"></i></a>
                    <a href="#" class="w-8 h-8 rounded-full bg-salon-50 flex items-center justify-center hover:bg-salon-600 hover:text-white transition-all"><i class="fa-brands fa-facebook-f"></i></a>
                    <a href="#" class="w-8 h-8 rounded-full bg-salon-50 flex items-center justify-center hover:bg-salon-600 hover:text-white transition-all"><i class="fa-brands fa-tiktok"></i></a>
                </div>
            </div>

            <!-- Quick Links -->
            <div>
                <h4 class="text-xs font-bold uppercase tracking-wider text-salon-900 mb-4">Discover</h4>
                <ul class="space-y-2 text-sm text-salon-700">
                    <li><a href="${pageContext.request.contextPath}/services" class="hover:text-salon-500 transition-colors">Our Services</a></li>
                    <li><a href="${pageContext.request.contextPath}/packages" class="hover:text-salon-500 transition-colors">Curated Packages</a></li>
                    <li><a href="${pageContext.request.contextPath}/reviews" class="hover:text-salon-500 transition-colors">Client Testimonials</a></li>
                    <li><a href="${pageContext.request.contextPath}/membership" class="hover:text-salon-500 transition-colors">Membership &amp; Rewards</a></li>
                </ul>
            </div>

            <!-- Contact & Hours -->
            <div>
                <h4 class="text-xs font-bold uppercase tracking-wider text-salon-900 mb-4">Salon Hours & Info</h4>
                <p class="text-sm text-salon-700 mb-2"><strong>Hours:</strong> Mon - Sat: 9:00 AM - 8:00 PM</p>
                <p class="text-sm text-salon-700 mb-2"><strong>Location:</strong> 123 Beauty Lane, Colombo 03</p>
                <p class="text-sm text-salon-700"><strong>Contact:</strong> +94 11 234 5678</p>
            </div>
        </div>

        <div class="mt-12 pt-8 border-t border-salon-100 flex flex-col md:flex-row justify-between items-center text-xs text-salon-500">
            <p>&copy; 2026 Glamour Beauty Salon. SLIIT SE1020 Project. All rights reserved.</p>
            <p class="mt-2 md:mt-0">Engineered with Spring Boot &amp; Tailwind CSS.</p>
        </div>
    </div>
</footer>
</body>
</html>
