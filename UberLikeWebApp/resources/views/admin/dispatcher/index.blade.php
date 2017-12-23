@extends('admin.layout.base')

@section('title', 'Dispatcher ')

@section('content')
<div class="content-area py-1">
    <div class="container-fluid">
        <div class="box box-block bg-white">
            <h5 class="mb-1">
                Dispatcher
                @if(Setting::get('demo_mode', 0) == 1)
                <span class="pull-right">(*personal information hidden in demo)</span>
                @endif
            </h5>
            <a href="{{ route('admin.dispatch-manager.create') }}" style="margin-left: 1em;" class="btn btn-primary pull-right"><i class="fa fa-plus"></i> Add New Dispatcher</a>
            <table class="table table-striped table-bordered dataTable" id="table-2">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Mobile</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    @foreach($dispatchers as $index => $dispatcher)
                    <tr>
                        <td>{{ $index + 1 }}</td>
                        <td>{{ $dispatcher->name }}</td>
                        @if(Setting::get('demo_mode', 0) == 1)
                        <td>{{ substr($dispatcher->email, 0, 3).'****'.substr($dispatcher->email, strpos($dispatcher->email, "@")) }}</td>
                        @else
                        <td>{{ $dispatcher->email }}</td>
                        @endif
                        @if(Setting::get('demo_mode', 0) == 1)
                        <td>+919876543210</td>
                        @else
                        <td>{{ $dispatcher->mobile }}</td>
                        @endif
                        <td>
                            <form action="{{ route('admin.dispatch-manager.destroy', $dispatcher->id) }}" method="POST">
                                {{ csrf_field() }}
                                <input type="hidden" name="_method" value="DELETE">
                                <a href="{{ route('admin.dispatch-manager.edit', $dispatcher->id) }}" class="btn btn-info"><i class="fa fa-pencil"></i> Edit</a>
                                <button class="btn btn-danger" onclick="return confirm('Are you sure?')"><i class="fa fa-trash"></i> Delete</button>
                            </form>
                        </td>
                    </tr>
                    @endforeach
                </tbody>
                <tfoot>
                    <tr>
                        <th>ID</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Mobile</th>
                        <th>Action</th>
                    </tr>
                </tfoot>
            </table>
        </div>
    </div>
</div>
@endsection