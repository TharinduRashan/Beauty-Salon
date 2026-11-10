private static final String FILE_NAME = "packages.txt";
private static final int EXPECTED_FIELDS = 9;

private final Path dataDir;
private final IdGenerator idGenerator;

@Autowired
public PackageRepository(Path dataDirectory, IdGenerator idGenerator) {
    this.dataDir = dataDirectory;
    this.idGenerator = idGenerator;
}

// Get the path of the package storage file
private Path getStorageFile() {
    return dataDir.resolve(FILE_NAME);
}

// Convert stored data into a ServicePackage object
private ServicePackage fromParts(String[] parts) {

    ServicePackage servicePackage = new ServicePackage();

    servicePackage.setId(parts[0]);
    servicePackage.setName(parts[1]);
    servicePackage.setDescription(parts[2]);
    servicePackage.setServiceIdsFromString(parts[3]);

    servicePackage.setTotalPrice(new BigDecimal(parts[4]));
    servicePackage.setDiscountPercent(new BigDecimal(parts[5]));

    servicePackage.setStatus(ServiceStatus.fromString(parts[6]));

    servicePackage.setCreatedAt(
            DateTimeUtil.parseDateTime(parts[7])
    );

    servicePackage.setUpdatedAt(
            DateTimeUtil.parseDateTime(parts[8])
    );

    return servicePackage;
}

// Convert a ServicePackage object into a storage line
private String toLine(ServicePackage servicePackage) {

    return FileStorageUtil.joinLine(
            servicePackage.getId(),
            servicePackage.getName(),
            servicePackage.getDescription(),
            servicePackage.getServiceIdsAsString(),
            servicePackage.getTotalPrice().toPlainString(),
            servicePackage.getDiscountPercent().toPlainString(),
            servicePackage.getStatus().name(),
            DateTimeUtil.formatDateTime(servicePackage.getCreatedAt()),
            DateTimeUtil.formatDateTime(servicePackage.getUpdatedAt())
    );
}

// Retrieve all packages from the storage file
public List<ServicePackage> findAll() {

    return FileStorageUtil.readAll(
            getStorageFile(),
            this::fromParts,
            EXPECTED_FIELDS
    );
}

// Retrieve packages that are currently active
public List<ServicePackage> findAllActive() {

    return findAll()
            .stream()
            .filter(ServicePackage::isActive)
            .toList();
}

// Find a package using its unique ID
public Optional<ServicePackage> findById(String id) {

    if (id == null || id.isBlank()) {
        return Optional.empty();
    }

    return findAll()
            .stream()
            .filter(pkg -> Objects.equals(pkg.getId(), id))
            .findFirst();
}

// Save a new package to the storage file
public ServicePackage save(ServicePackage servicePackage) throws IOException {

    if (servicePackage.getId() == null ||
            servicePackage.getId().isBlank()) {

        servicePackage.setId(idGenerator.generate());
    }

    var currentTime = DateTimeUtil.now();

    if (servicePackage.getCreatedAt() == null) {
        servicePackage.setCreatedAt(currentTime);
    }

    servicePackage.setUpdatedAt(currentTime);

    FileStorageUtil.appendLine(
            getStorageFile(),
            toLine(servicePackage)
    );

    return servicePackage;
}

// Update an existing package
public ServicePackage update(ServicePackage servicePackage) throws IOException {

    servicePackage.setUpdatedAt(DateTimeUtil.now());

    String updatedLine = toLine(servicePackage);

    FileStorageUtil.updateLines(
            getStorageFile(),
            line -> replaceMatchingPackage(line, servicePackage.getId(), updatedLine)
    );

    return servicePackage;
}

// Check whether a storage line belongs to the requested package
private String replaceMatchingPackage(String line, String packageId,
                                       String updatedLine) {

    String[] parts = FileStorageUtil.splitLine(line);

    if (parts.length > 0 &&
            Objects.equals(parts[0], packageId)) {

        return updatedLine;
    }

    return line;
}

// Delete a package using its unique ID
public void deleteById(String id) throws IOException {

    if (id == null || id.isBlank()) {
        return;
    }

    FileStorageUtil.deleteById(getStorageFile(), id);
}
