[ {
  path : '/',
  component : DashboardLayout,
  redirect : '/dashboard',
  children : [ {
    path : 'dashboard',
    name : 'Dashboard',
    component : Dashboard
  }, {
    path : 'dashboard',
    name : 'Dashboard',
    component : Terminal
  }, {
    path : 'user',
    name : 'User Profile',
    component : UserProfile
  }, {
    path : 'table',
    name : 'Table List',
    component : TableList
  }, {
    path : 'typography',
    name : 'Typography',
    component : Typography
  }, {
    path : 'icons',
    name : 'Icons',
    component : Icons
  }, {
    path : 'maps',
    name : 'Maps',
    meta : {
      hideFooter : true
    },
    component : Maps

  }, {
    path : 'notifications',
    name : 'Notifications',
    component : Notifications
  } ]
} ]