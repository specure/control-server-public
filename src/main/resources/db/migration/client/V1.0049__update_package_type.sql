update package
set package_type = 'FIXED_WIRELESS_BROADBAND'
where package_type = 'PREPAID';

update package
set package_type = 'FIXED_BROADBAND'
where package_type = 'POSTPAID';

update package
set package_type = 'MOBILE_PREPAID'
where package_type = 'MOBILE_PREPAID';

update package
set package_type = 'MOBILE_BROADBAND'
where package_type = 'MOBILE_POSTPAID';
