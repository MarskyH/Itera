import RecentProjects from 'src/components/RecentProjects.vue'

export default [
  {
    path: '/login',
    name: 'login',
    component: async () => await import('src/views/Login/LoginView.vue'),
  },
  {
    path: '/cadastro',
    name: 'cadastro',
    component: async () => await import('src/views/Cadastro/CadastroView.vue'),
  },
  {
    path: '/',
    name: 'protected',
    redirect: () => { return 'home' },
    meta: {
      requiresAuth: true
    },
    children: [
      {
        path: '/',
        name: 'navigation',
        component: async () => await import('src/views/Navigation/NavigationView.vue'),
        redirect: () => { return 'home' },
        children: [
          {
            path: '/home',
            name: 'home',
            component: async () => await import('src/views/Home/HomeView.vue'),
            meta: {
              title: 'Home',
              icon: 'house',
              sideViewComponent: RecentProjects
            }
          },
          {
            path: '/novo-projeto',
            name: 'new-project',
            component: async () => await import('src/views/NewProject/NewProjectView.vue'),
            redirect: { name: 'general' },
            meta: {
              title: 'Novo Projeto',
              icon: 'folder-plus',
              sideViewComponent: RecentProjects
            },
            children: [
              {
                path: 'geral',
                name: 'general',
                component: async () => await import('src/views/NewProject/components/GeneralStep.vue')
              },
              {
                path: 'habilidades-e-competencias',
                name: 'roles',
                component: async () => await import('src/views/NewProject/components/RolesStep.vue')
              },
              {
                path: 'equipe',
                name: 'team',
                component: async () => await import('src/views/NewProject/components/TeamStep.vue')
              },
              {
                path: 'riscos',
                name: 'risks',
                component: async () => await import('src/views/NewProject/components/RisksStep.vue'),
              },
              {
                path: 'requisitos-funcionais',
                name: 'functional-requirements',
                component: async () => await import('src/views/NewProject/components/FunctionalRequirementsStep.vue'),
              },
              {
                path: 'requisitos-nao-funcionais',
                name: 'non-functional-requirements',
                component: async () => await import('src/views/NewProject/components/NonFunctionalRequirementsStep.vue'),
              }
            ]
          },
        ]
      },
    ]
  },
]