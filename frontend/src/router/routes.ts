
import RecentProjects from 'src/components/RecentProjects.vue'
import DetailsView from 'src/views/Project/components/DetailsView.vue'

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
            path: '/meus-projetos',
            name: 'my-projects',
            component: async () => await import('src/views/MyProjects/MyProjectsView.vue'),
            meta: {
              title: 'Meus Projetos',
              icon: 'folder-open',
              sideViewComponent: RecentProjects
            }
          },
          {
            path: '/projeto',
            name: 'project',
            children: [
              {
                path: ':projectId',
                name: 'project-info',
                component: async () => await import('src/views/Project/ProjectView.vue'),
                redirect: () => { return { name: 'view-roles' } },
                meta: {
                  title: 'Projeto',
                  icon: 'folder-open',
                  sideViewComponent: DetailsView
                },
                children: [
                  {
                    path: 'habilidades-e-competencias',
                    name: 'view-roles',
                    component: async () => await import('src/views/NewProject/components/RolesStep.vue')
                  },
                  {
                    path: 'equipe',
                    name: 'view-team',
                    component: async () => await import('src/views/NewProject/components/TeamStep.vue')
                  },
                  {
                    path: 'riscos',
                    name: 'view-risks',
                    component: async () => await import('src/views/NewProject/components/RisksStep.vue'),
                  },
                  {
                    path: 'requisitos-funcionais',
                    name: 'view-functional-requirements',
                    component: async () => await import('src/views/NewProject/components/FunctionalRequirementsStep.vue'),
                  },
                  {
                    path: 'requisitos-nao-funcionais',
                    name: 'view-non-functional-requirements',
                    component: async () => await import('src/views/NewProject/components/NonFunctionalRequirementsStep.vue'),
                  }
                ]
              },
              {
                path: 'novo',
                name: 'new-project',
                redirect: { name: 'general' },
                component: async () => await import('src/views/NewProject/NewProjectView.vue'),
                meta: {
                  title: 'Novo Projeto',
                  icon: 'folder-plus',
                  sideViewComponent: RecentProjects
                },
                children: [
                  {
                    path: '',
                    name: 'general',
                    component: async () => await import('src/views/NewProject/components/GeneralStep.vue')
                  },
                  {
                    path: ':projectId/habilidades-e-competencias',
                    name: 'roles',
                    component: async () => await import('src/views/NewProject/components/RolesStep.vue')
                  },
                  {
                    path: ':projectId/equipe',
                    name: 'team',
                    component: async () => await import('src/views/NewProject/components/TeamStep.vue')
                  },
                  {
                    path: ':projectId/riscos',
                    name: 'risks',
                    component: async () => await import('src/views/NewProject/components/RisksStep.vue'),
                  },
                  {
                    path: ':projectId/requisitos-funcionais',
                    name: 'functional-requirements',
                    component: async () => await import('src/views/NewProject/components/FunctionalRequirementsStep.vue'),
                  },
                  {
                    path: ':projectId/requisitos-nao-funcionais',
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
  },
]