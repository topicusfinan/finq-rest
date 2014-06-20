module.exports = function(grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        clean: ["dist",'.tmp'],

        gruntMavenProperties: grunt.file.readJSON('grunt-maven.json'),
        mavenProperties: grunt.file.readJSON('maven-properties.json'),
        mavenPrepare:{
            options: {
                resources:['**']
            }
        },
        copy:{
            dist:{
                main:{
                    expand:true,
                    cwd: 'app/',
                    src: ['**', '!components/**', '!**/*.{js,css}'],
                    dest: 'dist/'
                },
                index:{
                    expand:true,
                    cwd: './',
                    src: ['index.html'],
                    dest: 'dist/'
                }
            },
            main:{
                expand:true,
                cwd: './',
                src: ['app/**', 'index.html'],
                dest: 'dist/'
            }
        },
        rev: {
            files: {
                src: ['dist/**/*.{js,css}']
            }
        },
        useminPrepare:{
            html: 'index.html'
        },
        usemin: {
            html: ['dist/index.html']
        },
        mavenDist: {
            options: {
                warName: 'runner-<%= mavenProperties.version %>',
                deliverables: [],
                gruntDistDir: 'dist'
            },
            dist: {}
        },
        watch: {
            maven: {
                files: ['<%= gruntMavenProperties.filesToWatch %>'],
                tasks: ['default']
            }
        },
        bower:{
            install:{
                options:{
                    targetDir: './app/components'
                }
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-usemin');
    grunt.loadNpmTasks('grunt-rev');
    grunt.loadNpmTasks('grunt-maven');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-npm-install');
    grunt.loadNpmTasks('grunt-bower-task');

    grunt.registerTask('default',['mavenPrepare','clean','npm-install','bower','copy:main','mavenDist'])

    grunt.registerTask('dist', ['mavenPrepare','clean','npm-install','bower','copy:dist', 'useminPrepare', 'concat', 'uglify', 'cssmin', 'rev','usemin','mavenDist']);

};
