# Hot Reload / Debug Workflow

Use the shared IntelliJ run configuration:

- `RPG Helper Debug`

Recommended IntelliJ settings:

1. Open `Settings -> Build, Execution, Deployment -> Compiler`
2. Enable `Build project automatically`
3. Open `Settings -> Advanced Settings`
4. Enable `Allow auto-make to start even if developed application is currently running`
5. Open `Settings -> Build, Execution, Deployment -> Debugger -> HotSwap`
6. Set `Reload classes after compilation` to `Always`

How to use it:

1. Start the app with `Debug` using `RPG Helper Debug`
2. Edit code
3. Press `Ctrl+F9` to build, or let IntelliJ auto-make do it
4. IntelliJ will hot-swap compatible changes into the running app

What works without restart in standard JVM HotSwap:

- Method body changes
- Most UI text/layout logic inside existing methods
- Event handler logic changes

What still requires restart in standard JVM HotSwap:

- Adding or removing fields
- Adding or removing methods
- Changing class structure
- Changing inheritance or method signatures
- Adding brand new classes that active code depends on immediately

If you want true structural hot reload, the next step is:

- DCEVM + HotswapAgent

That is the right path if you want to change class structure without restarting. Plain IntelliJ HotSwap is still useful, but it is limited by the JVM.
